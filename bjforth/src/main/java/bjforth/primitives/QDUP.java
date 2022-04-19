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

public class QDUP implements MachinePrimitiveWithNext {

  @Override
  public void executeWithNext(Machine machine) {
    try {
      var element = machine.popFromParameterStack();
      if (!isZero(element)) {
        machine.pushToParameterStack(element);
      }
      machine.pushToParameterStack(element);
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }

  private boolean isZero(Object obj) {
    if (obj instanceof BigDecimal bd) {
      return BigDecimal.ZERO.equals(bd);
    } else if (obj instanceof BigInteger bi) {
      return BigInteger.ZERO.equals(bi);
    } else if (obj instanceof Byte b) {
      return b == 0;
    } else if (obj instanceof Double d) {
      return d == 0d;
    } else if (obj instanceof Float f) {
      return f == 0f;
    } else if (obj instanceof Integer i) {
      return i == 0;
    } else if (obj instanceof Long l) {
      return l == 0l;
    } else if (obj instanceof Short s) {
      return s == 0;
    } else {
      throw new MachineException("ParameterStack top not a number.");
    }
  }
}
