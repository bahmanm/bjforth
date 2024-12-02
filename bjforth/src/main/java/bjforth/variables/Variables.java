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

import java.util.Set;

public class Variables {

  private static class DefaultVariableImpl implements Variable {

    private final Integer address;
    private final Integer initialValue;
    private final String name;

    DefaultVariableImpl(String name, Integer address, Integer initialValue) {
      this.name = name;
      this.address = address;
      this.initialValue = initialValue;
    }

    @Override
    public Integer getAddress() {
      return address;
    }

    @Override
    public Object getInitialValue() {
      return initialValue;
    }

    @Override
    public String getName() {
      return name;
    }
  }

  private static final Variable varHERE = new DefaultVariableImpl("HERE", 0, 4);

  private static final Variable varSTATE = new DefaultVariableImpl("STATE", 1, 0);

  private static final Variable varBASE = new DefaultVariableImpl("BASE", 2, 10);

  private static final Variable varLATEST =
      new DefaultVariableImpl("LATEST", 3, 1); // TODO Dummy initial value

  public static Variable get(String name) {
    return variables.stream().filter(variable -> variable.getName().equals(name)).findFirst().get();
  }

  public static Set<Variable> variables = Set.of(varHERE, varSTATE, varBASE, varLATEST);
}
