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
import bjforth.primitives.lib.AbstractBinaryNumberFunction;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.NoSuchElementException;

class DIV implements Primitive {

  private static final DivideNumberFunction add = new DivideNumberFunction();

  @Override
  public void execute(Machine machine) {
    try {
      var element1 = machine.popFromParameterStack();
      var element2 = machine.popFromParameterStack();
      var result = add.apply(element1, element2);
      machine.pushToParameterStack(result);
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }

  private static class DivideNumberFunction extends AbstractBinaryNumberFunction<Number> {

    @Override
    protected Number apply(BigDecimal value1, BigDecimal value2) {
      return value1.divide(value2, MathContext.DECIMAL128);
    }

    @Override
    protected Number apply(BigInteger value1, BigInteger value2) {
      return value1.divide(value2);
    }

    @Override
    protected Number apply(Double value1, Double value2) {
      return value1 / value2;
    }

    @Override
    protected Number apply(Float value1, Float value2) {
      return value1 / value2;
    }

    @Override
    protected Number apply(Long value1, Long value2) {
      return value1 / value2;
    }

    @Override
    protected Number apply(Integer value1, Integer value2) {
      return value1 / value2;
    }

    @Override
    protected Number apply(Short value1, Short value2) {
      return value1 / value2;
    }

    @Override
    protected Number apply(Byte value1, Byte value2) {
      return value1 / value2;
    }
  }

  @Override
  public String getName() {
    return "/";
  }
}
