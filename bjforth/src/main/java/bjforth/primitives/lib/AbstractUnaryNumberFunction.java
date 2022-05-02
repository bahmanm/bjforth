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

import bjforth.machine.MachineException;
import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class AbstractUnaryNumberFunction<R> {

  public R apply(Object value) {
    if (value == null) {
      throw new MachineException("ParameterStack top is NULL.");
    } else if (value instanceof BigDecimal bd) {
      return apply(bd);
    } else if (value instanceof BigInteger bi) {
      return apply(bi);
    } else if (value instanceof Byte b) {
      return apply(b);
    } else if (value instanceof Double d) {
      return apply(d);
    } else if (value instanceof Float f) {
      return apply(f);
    } else if (value instanceof Integer i) {
      return apply(i);
    } else if (value instanceof Long l) {
      return apply(l);
    } else if (value instanceof Short s) {
      return apply(s);
    } else {
      throw new MachineException("ParameterStack top not a number.");
    }
  }

  protected abstract R apply(BigDecimal value);

  protected abstract R apply(BigInteger value);

  protected abstract R apply(Double value);

  protected abstract R apply(Float value);

  protected abstract R apply(Integer value);

  protected abstract R apply(Long value);

  protected abstract R apply(Short value);

  protected abstract R apply(Byte value);
}
