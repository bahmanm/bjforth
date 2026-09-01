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
package bjforth.primitives.lib;

import bjforth.machine.MachineException;
import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class AbstractUnaryNumberFunction<R> {

  public R apply(Object value) {
    return switch (value) {
      case null -> throw new MachineException("ParameterStack top is NULL.");
      case BigDecimal bd -> apply(bd);
      case BigInteger bi -> apply(bi);
      case Byte b -> apply(b);
      case Double d -> apply(d);
      case Float f -> apply(f);
      case Integer i -> apply(i);
      case Long l -> apply(l);
      case Short s -> apply(s);
      default -> throw new MachineException("ParameterStack top not a number.");
    };
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
