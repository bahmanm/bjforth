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

import static bjforth.config.Constants.BACKGROUND_COLOR;
import static bjforth.config.Constants.FOREGROUND_COLOR;
import static com.diogonunes.jcolor.Ansi.colorize;

import bjforth.machine.Machine;

public class DUMP implements Primitive {
  @Override
  public void execute(Machine machine) {
    var len = (Integer) machine.popFromParameterStack();
    var addr = (Integer) machine.popFromParameterStack();
    if (addr != 0) {
      System.out.print(
          colorize("%08d".formatted(addr - (addr % 8)), FOREGROUND_COLOR, BACKGROUND_COLOR));
      System.out.print("    ");
    }
    if (addr != 0 && addr % 8 != 0) {
      System.out.print("            ".repeat(addr % 8));
    }
    for (var i = addr; i < addr + len; i++) {
      var obj = machine.getMemoryAt(i);
      if (i % 8 == 0) {
        System.out.println();
        System.out.print(colorize("%08d".formatted(i), FOREGROUND_COLOR, BACKGROUND_COLOR));
        System.out.print("    ");
      }
      var formatted =
          switch (obj) {
            case Integer n -> "%08d".formatted(n);
            case Primitive primitive -> "%-8s".formatted(primitive.getName());
            case null -> "%-8s".formatted("null");
            default -> "%8s".formatted(obj.toString());
          };
      System.out.print(colorize(formatted, FOREGROUND_COLOR, BACKGROUND_COLOR));
      System.out.print("    ");
    }
    System.out.println();
  }
}
