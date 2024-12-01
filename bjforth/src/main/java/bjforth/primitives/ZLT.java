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
package bjforth.primitives;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;

class ZLT implements Primitive {

  private static final IsLessThanZero isLessThanZero = new IsLessThanZero();

  @Override
  public void execute(Machine machine) {
    try {
      var element = machine.popFromParameterStack();
      var result = isLessThanZero.call(element) ? 1 : 0;
      machine.pushToParameterStack(result);
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }

  private static class IsLessThanZero {

    boolean call(Object obj) {
      if (obj instanceof BigDecimal n) {
        return n.compareTo(BigDecimal.ZERO) == -1;
      } else if (obj instanceof BigInteger n) {
        return n.compareTo(BigInteger.ZERO) == -1;
      } else if (obj instanceof Number n) {
        return ((Number) n).doubleValue() < 0d;
      } else {
        throw new MachineException("Unexpected subtype of Number");
      }
    }
  }

  @Override
  public String getName() {
    return "0<";
  }
}
