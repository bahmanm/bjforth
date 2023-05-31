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
import bjforth.primitives.lib.AbstractBinaryNumberFunction;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;

public class SUB implements Primitive {

  private static final SubtractNumberFunction add = new SubtractNumberFunction();

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

  private static class SubtractNumberFunction extends AbstractBinaryNumberFunction<Number> {

    @Override
    protected Number apply(BigDecimal value1, BigDecimal value2) {
      return value1.subtract(value2);
    }

    @Override
    protected Number apply(BigInteger value1, BigInteger value2) {
      return value1.subtract(value2);
    }

    @Override
    protected Number apply(Double value1, Double value2) {
      return value1 - value2;
    }

    @Override
    protected Number apply(Float value1, Float value2) {
      return value1 - value2;
    }

    @Override
    protected Number apply(Long value1, Long value2) {
      return value1 - value2;
    }

    @Override
    protected Number apply(Integer value1, Integer value2) {
      return value1 - value2;
    }

    @Override
    protected Number apply(Short value1, Short value2) {
      return value1 - value2;
    }

    @Override
    protected Number apply(Byte value1, Byte value2) {
      return value1 - value2;
    }
  }
}
