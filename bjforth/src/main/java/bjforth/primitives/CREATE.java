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

import bjforth.machine.DictionaryItem;
import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import bjforth.variables.Variables;
import java.util.NoSuchElementException;

public class CREATE implements Primitive {

  @Override
  public void execute(Machine machine) {
    try {
      var lengthObject = machine.popFromParameterStack();
      var addressObject = machine.popFromParameterStack();
      var HEREAddr = Variables.HERE().getAddress();
      var HEREValue = (Integer) machine.getMemoryAt(HEREAddr);
      var LATESTAddr = Variables.LATEST().getAddress();
      if (lengthObject instanceof Integer _length && addressObject instanceof Integer nameAddr) {
        var name = (String) machine.getMemoryAt(nameAddr);
        machine.setMemoryAt(HEREValue, name);
        var newHEREValue = HEREValue + 1;
        machine.setMemoryAt(HEREAddr, newHEREValue);
        machine.setMemoryAt(LATESTAddr, HEREValue);
        var dictItem = new DictionaryItem(name, HEREValue, false, true);
        machine.createDictionaryItem(name, dictItem);
      } else {
        throw new MachineException("Invalid argument");
      }
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }
}
