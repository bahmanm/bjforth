/*
 * Copyright 2023 Bahman Movaqar
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
package bjforth.primitives;

import java.util.function.Supplier;

public class PrimitiveFactory {

  private static PrimitiveContainer containerADD = new PrimitiveContainer(ADD::new);

  static Primitive ADD() {
    return containerADD.get();
  }

  private static PrimitiveContainer containerKEY = new PrimitiveContainer(KEY::new);

  static Primitive KEY() {
    return containerKEY.get();
  }

  private static class PrimitiveContainer {

    private Primitive instance;
    private Supplier<Primitive> supplier;

    PrimitiveContainer(Supplier<Primitive> supplier) {
      this.instance = null;
      this.supplier = supplier;
    }

    private synchronized Primitive get() {
      if (instance == null) {
        instance = supplier.get();
      }
      return instance;
    }
  }
}
