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
import java.util.NoSuchElementException;

public class BASESTORE implements Primitive {
  @Override
  public void execute(Machine machine) {
    try {
      var newValueObj = machine.popFromParameterStack();
      if (newValueObj instanceof Integer newValue) {
        machine.setMemoryAt(Variables.get("BASE").getAddress(), newValue);
      } else {
        throw new MachineException("Invalid parameter.");
      }
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }

  @Override
  public String getName() {
    return "BASE!";
  }
}
