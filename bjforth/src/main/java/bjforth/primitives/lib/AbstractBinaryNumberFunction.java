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
package bjforth.primitives.lib;

import static java.util.Objects.isNull;

import bjforth.machine.MachineException;
import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class AbstractBinaryNumberFunction<R> {

  public R apply(Object value1, Object value2) {
    if (isNull(value1)) {
      throw new MachineException("ParameterStack top is NULL.");
    } else if (isNull(value2)) {
      throw new MachineException("ParameterStack second top is NULL.");
    } else if (value1 instanceof Number n1 && value2 instanceof Number n2) {
      return apply(n1, n2);
    } else {
      throw new MachineException("ParameterStack top not a number.");
    }
  }

  private R apply(Number value1, Number value2) {
    if (value1 instanceof BigDecimal bd1 && value2 instanceof BigDecimal bd2) {
      return apply(bd1, bd2);
    } else if (value1 instanceof BigInteger bi1 && value2 instanceof BigInteger bi2) {
      return apply(bi1, bi2);
    } else if (value1 instanceof Double || value2 instanceof Double) {
      return apply(value1.doubleValue(), value2.doubleValue());
    } else if (value1 instanceof Float || value2 instanceof Float) {
      return apply(value1.floatValue(), value2.floatValue());
    } else if (value1 instanceof Long || value2 instanceof Long) {
      return apply(value1.longValue(), value2.longValue());
    } else if (value1 instanceof Integer || value2 instanceof Integer) {
      return apply(value1.intValue(), value2.intValue());
    } else if (value1 instanceof Short || value2 instanceof Short) {
      return apply(value1.shortValue(), value2.shortValue());
    } else if (value1 instanceof Byte || value2 instanceof Byte) {
      return apply(value1.byteValue(), value2.byteValue());
    } else {
      throw new MachineException("Unexpected subtype of Number");
    }
  }

  protected abstract R apply(BigDecimal value1, BigDecimal value2);

  protected abstract R apply(BigInteger value1, BigInteger value2);

  protected abstract R apply(Double value1, Double value2);

  protected abstract R apply(Float value1, Float value2);

  protected abstract R apply(Long value1, Long value2);

  protected abstract R apply(Integer value1, Integer value2);

  protected abstract R apply(Short value1, Short value2);

  protected abstract R apply(Byte value1, Byte value2);
}
