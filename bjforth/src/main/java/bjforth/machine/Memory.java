/*
 * Copyright 2022 Bahman Movaqar
 *
 * This file is part of BJForth.
 *
 * BJForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BJForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BJForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.machine;

import java.util.HashMap;
import java.util.Map;

class Memory {
  private final Map<Integer, Object> cells = new HashMap<>();

  Memory() {
  }

  Memory(Memory other) {
    other.cells.forEach(cells::put);
  }

  void set(Integer address, Object value) {
    cells.put(address, value);
  }

  Object get(Integer address) {
    return cells.get(address);
  }
}
