/*
 * Copyright 2025 Bahman Movaqar
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

import static bjforth.config.Constants.BACKGROUND_COLOR;
import static bjforth.config.Constants.FOREGROUND_COLOR;
import static com.diogonunes.jcolor.Ansi.colorize;

import bjforth.machine.Machine;

public class WORDS implements Primitive {

  @Override
  public void execute(Machine machine) {
    var columnNo = 0;
    for (var name : machine.getAllDictionaryItemNames().stream().sorted().toList()) {
      if (columnNo == 2) {
        System.out.println(colorize(" %-26s".formatted(name), FOREGROUND_COLOR, BACKGROUND_COLOR));
        columnNo = 0;
      } else {
        System.out.print(colorize(" %-26s".formatted(name), FOREGROUND_COLOR, BACKGROUND_COLOR));
        System.out.print(" ");
        columnNo += 1;
      }
    }
  }
}
