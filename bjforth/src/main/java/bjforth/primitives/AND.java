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

public class AND implements Primitive {
  @Override
  public void execute(Machine machine) {
    try {
      var obj1 = (Integer) machine.popFromParameterStack();
      var obj2 = (Integer) machine.popFromParameterStack();
      var result = (Integer) (obj1 & obj2);
      machine.pushToParameterStack(result);
    } catch (NoSuchElementException e) {
      throw new MachineException("Stack error.");
    }
  }
}
