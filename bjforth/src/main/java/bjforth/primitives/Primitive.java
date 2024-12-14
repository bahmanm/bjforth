/*
 * Copyright 2022 Bahman Movaqar
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
import bjforth.machine.NativeSubroutine;

public interface Primitive extends NativeSubroutine {

  @Override
  default void call(Machine machine) {
    execute(machine);
    if (!isBypassNextInstructionPointer()) {
      var NIP = machine.getNextInstructionPointer();
      machine.jumpTo(NIP);
    }
  }

  void execute(Machine machine);

  default String getName() {
    return getDescriptiveName();
  }

  default String getDescriptiveName() {
    return this.getClass().getSimpleName();
  }

  default Boolean isImmediate() {
    return false;
  }

  default Boolean isHidden() {
    return false;
  }

  default Boolean isBypassNextInstructionPointer() {
    return false;
  }
}
