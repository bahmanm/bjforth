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
import java.util.NoSuchElementException;

public class ZBRANCH implements Primitive {
  @Override
  public void execute(Machine machine) {
    try {
      var parameterObj = machine.popFromParameterStack();
      if (parameterObj instanceof Integer parameter) {
        if (parameter == 0) {
          var NIP = machine.getNextInstructionPointer();
          var NIPValue = (Integer) machine.getMemoryAt(NIP);
          var newNIP = NIP + NIPValue;
          machine.setNextInstructionPointer(newNIP);
        } else {
          machine.setNextInstructionPointer(machine.getNextInstructionPointer() + 1);
        }
      } else {
        throw new MachineException("Invalid parameter type");
      }
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }
}
