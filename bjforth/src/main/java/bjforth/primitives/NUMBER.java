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
package bjforth.primitives;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import bjforth.variables.Variables;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Objects;

class NUMBER implements Primitive {

  @Override
  public void execute(Machine machine) {
    try {
      var strObject = machine.popFromParameterStack();
      if (strObject instanceof String s) {
        var base = (Integer) machine.getMemoryAt(Variables.get("BASE").getAddress());
        Number number;
        try {
          number = StringToNumber.valueOf(s, base);
        } catch (NumberFormatException ex) {
          number = null;
        }
        var status = number == null ? -1 : 0;
        machine.pushToParameterStack(number);
        machine.pushToParameterStack(status);
      } else {
        throw new MachineException("Invalid argument");
      }
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }

  private static class StringToNumber {

    static Number valueOf(String s, int base) {
      if (s == null || s.isEmpty()) {
        return null;
      }
      if (base != 10) {
        return toInteger(s, base);
      } else {
        Number result = null;
        result = toInteger(s, 10);
        return Objects.requireNonNullElseGet(result, () -> new BigDecimal(s));
      }
    }

    private static Number toInteger(String s, int base) {
      try {
        return Integer.valueOf(s, base);
      } catch (NumberFormatException ignore1) {
      }
      try {
        return Long.valueOf(s, base);
      } catch (NumberFormatException ignore2) {
      }
      try {
        return new BigInteger(s, base);
      } catch (NumberFormatException ignore3) {
      }
      return null;
    }
  }
}
