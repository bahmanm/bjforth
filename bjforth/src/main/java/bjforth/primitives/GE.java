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
package bjforth.primitives;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;

public class GE implements Primitive {

  private static final GreaterThan greaterThan = new GreaterThan();

  @Override
  public void execute(Machine machine) {
    try {
      var element1 = machine.popFromParameterStack();
      var element2 = machine.popFromParameterStack();
      var result = greaterThan.call(element1, element2) ? 1 : 0;
      machine.pushToParameterStack(result);
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }

  private static class GreaterThan {

    boolean call(Object obj1, Object obj2) {
      if (obj1 instanceof BigDecimal n && obj2 instanceof BigDecimal m) {
        return n.compareTo(m) > -1;
      } else if (obj1 instanceof BigInteger n && obj2 instanceof BigInteger m) {
        return n.compareTo(m) > -1;
      } else if (obj1 instanceof BigDecimal n && obj2 instanceof Number m) {
        return n.compareTo(BigDecimal.valueOf(m.doubleValue())) > -1;
      } else if (obj1 instanceof Number n && obj2 instanceof BigDecimal m) {
        return BigDecimal.valueOf(n.doubleValue()).compareTo(m) > -1;
      } else if (obj1 instanceof BigInteger n && isConvertibleToBigInteger(obj2)) {
        return n.compareTo(BigInteger.valueOf(((Number) obj2).longValue())) > -1;
      } else if (isConvertibleToBigInteger(obj1) && obj2 instanceof BigInteger m) {
        return BigInteger.valueOf(((Number) obj1).longValue()).compareTo(m) > -1;
      } else if (obj1 instanceof Number n && obj2 instanceof Number m) {
        return ((Number) n).doubleValue() >= ((Number) m).doubleValue();
      } else {
        throw new MachineException("Unexpected subtype of Number");
      }
    }

    private static boolean isConvertibleToBigInteger(Object obj) {
      return obj instanceof Byte
          || obj instanceof Short
          || obj instanceof Integer
          || obj instanceof Long;
    }
  }
}
