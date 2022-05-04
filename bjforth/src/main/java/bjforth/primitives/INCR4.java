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
import bjforth.primitives.lib.AbstractUnaryNumberFunction;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;

public class INCR4 implements MachinePrimitiveWithNext {

  private static final Incr4NumberFunction incr = new Incr4NumberFunction();

  @Override
  public void executeWithNext(Machine machine) {
    try {
      var element = machine.popFromParameterStack();
      var elementIncred = incr.apply(element);
      machine.pushToParameterStack(elementIncred);
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }

  private static class Incr4NumberFunction extends AbstractUnaryNumberFunction<Number> {

    @Override
    protected Number apply(BigDecimal value) {
      return value.add(BigDecimal.valueOf(4l));
    }

    @Override
    protected Number apply(BigInteger value) {
      return value.add(BigInteger.valueOf(4l));
    }

    @Override
    protected Number apply(Double value) {
      return value + 4d;
    }

    @Override
    protected Number apply(Float value) {
      return value + 4f;
    }

    @Override
    protected Number apply(Integer value) {
      return value + 4;
    }

    @Override
    protected Number apply(Long value) {
      return value + 4l;
    }

    @Override
    protected Number apply(Short value) {
      return (short) (value + 4);
    }

    @Override
    protected Number apply(Byte value) {
      return (byte) (value + 4);
    }
  }
}
