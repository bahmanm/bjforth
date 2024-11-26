/*
 * Copyright 2023 Bahman Movaqar
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
package bjforth.variables;

public class Variables {

  private static class DefaultVariableImpl implements Variable {

    private final Integer address;

    DefaultVariableImpl(Integer address) {
      this.address = address;
    }

    @Override
    public Integer getAddress() {
      return address;
    }
  }

  private static final Variable varHERE = new DefaultVariableImpl(0);

  public static Variable HERE() {
    return varHERE;
  }

  private static final Variable varSTATE = new DefaultVariableImpl(1);

  public static Variable STATE() {
    return varSTATE;
  }

  private static final Variable varBASE = new DefaultVariableImpl(2);

  public static Variable BASE() {
    return varBASE;
  }

  private static final Variable varLATEST = new DefaultVariableImpl(3);

  public static Variable LATEST() {
    return varLATEST;
  }
}
