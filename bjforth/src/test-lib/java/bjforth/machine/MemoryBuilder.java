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
package bjforth.machine;

import static bjforth.machine.MachineStateInspectionUtils.memoryAddresses;

import java.util.Map;

public class MemoryBuilder {
  private Memory memory = new Memory();

  private MemoryBuilder() {}

  public static MemoryBuilder aMemory() {
    return new MemoryBuilder();
  }

  public MemoryBuilder with(MachineState state) {
    memoryAddresses(state).forEach(addr -> memory.set(addr, state.getMemory().get(addr)));
    return this;
  }

  public MemoryBuilder with(Integer address, Object object) {
    memory.set(address, object);
    return this;
  }

  public MemoryBuilder with(Map<Integer, Object> cells) {
    cells.forEach((address, object) -> memory.set(address, object));
    return this;
  }

  public Memory build() {
    return memory;
  }
}
