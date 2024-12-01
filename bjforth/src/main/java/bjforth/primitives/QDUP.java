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
import bjforth.primitives.lib.AbstractUnaryNumberFunction;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;

class QDUP implements Primitive {

  private static final IsZero isZero = new IsZero();

  @Override
  public void execute(Machine machine) {
    try {
      var element = machine.popFromParameterStack();
      if (!isZero.apply(element)) {
        machine.pushToParameterStack(element);
      }
      machine.pushToParameterStack(element);
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }

  private static class IsZero extends AbstractUnaryNumberFunction<Boolean> {

    @Override
    protected Boolean apply(BigDecimal value) {
      return BigDecimal.ZERO.equals(value);
    }

    @Override
    protected Boolean apply(BigInteger value) {
      return BigInteger.ZERO.equals(value);
    }

    @Override
    protected Boolean apply(Double value) {
      return value == 0d;
    }

    @Override
    protected Boolean apply(Float value) {
      return value == 0f;
    }

    @Override
    protected Boolean apply(Integer value) {
      return value == 0;
    }

    @Override
    protected Boolean apply(Long value) {
      return value == 0l;
    }

    @Override
    protected Boolean apply(Short value) {
      return value == 0;
    }

    @Override
    protected Boolean apply(Byte value) {
      return value == 0;
    }
  }

  @Override
  public String getName() {
    return "?DUP";
  }
}
