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

public class DSPFETCHFETCH implements Primitive {

  @Override
  public void execute(Machine machine) {
    var pointerObj = machine.popFromParameterStack();
    if (pointerObj instanceof Integer pointer) {
      var element = machine.getItemParamaterStack(pointer);
      machine.pushToParameterStack(element);
    } else if (pointerObj == null) {
      throw new MachineException("Invalid stack pointer: null");
    } else {
      throw new MachineException("Invalid stack pointer: %s".formatted(pointerObj));
    }
  }

  @Override
  public String getName() {
    return "DSP@@";
  }
}
