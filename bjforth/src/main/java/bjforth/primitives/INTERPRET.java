/*
 * Copyright 2024 Bahman Movaqar
 *
 * This file is part of bjForth.
 *
 * bjForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * bjForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with bjForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.primitives;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import bjforth.variables.Variables;

public class INTERPRET implements Primitive {
  @Override
  public void execute(Machine machine) {
    var STATE = (Integer) machine.getMemoryAt(Variables.get("STATE").getAddress());
    var HEREaddr = Variables.get("HERE").getAddress();
    var HEREvalue = (Integer) machine.getMemoryAt(HEREaddr);

    PrimitiveFactory.WORD().execute(machine);
    var obj = machine.peekIntoParameterStack();
    try {
      PrimitiveFactory.FIND().execute(machine);
      var dictItem = machine.getDictionaryItem((Integer) machine.popFromParameterStack()).get();
      if (STATE == 0 || dictItem.getIsImmediate()) {
        machine.jumpTo(dictItem.getAddress());
      } else {
        machine.setMemoryAt(HEREvalue, dictItem.getAddress());
        machine.setMemoryAt(HEREaddr, (Integer) machine.getMemoryAt(HEREaddr) + 1);
      }
    } catch (MachineException _ex) { // Not in dictionary. Check if it's a number.
      machine.pushToParameterStack(obj);
      try {
        PrimitiveFactory.NUMBER().execute(machine);
        var numberStatus = (Integer) machine.popFromParameterStack();
        if (numberStatus != 0) {
          machine.popFromParameterStack();
          throw new MachineException("Invalid number.");
        }
        var number = (Number) machine.popFromParameterStack();
        if (STATE == 1) { // Compiling mode
          machine.setMemoryAt(HEREvalue, machine.getDictionaryItem("LIT").get().getAddress());
          machine.setMemoryAt(HEREvalue + 1, number);
          machine.setMemoryAt(HEREaddr, (Integer) machine.getMemoryAt(HEREaddr) + 2);
        } else { // Immediate mode
          machine.pushToParameterStack(number);
        }
      } catch (MachineException __ex) { // Not a number. Exit with error.
        throw new MachineException("Invalid word or number.");
      }
    }
  }

  @Override
  public Boolean isBypassNextInstructionPointer() {
    return true;
  }
}
