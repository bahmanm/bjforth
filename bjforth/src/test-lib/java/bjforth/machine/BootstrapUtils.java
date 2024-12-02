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
package bjforth.machine;

import bjforth.primitives.PrimitiveFactory;
import bjforth.variables.Variables;

public class BootstrapUtils {

  public static Integer getPrimitiveAddress(String word) {
    var containers = PrimitiveFactory.getPrimitiveContainers();
    for (int i = 0; i < containers.size(); i++) {
      if (containers.get(i).get().getName().equals(word)) {
        return Variables.variables.size() + i;
      }
    }
    return null;
  }
}
