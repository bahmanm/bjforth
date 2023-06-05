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

import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.MachineException;
import bjforth.utils.RandomUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class DECR4Test {

  @DisplayName("decrements top of stack by 4, ie a -> a-4.")
  @ParameterizedTest(name = "{displayName} parameter(type={2}, value={0}, expected={1})")
  @ArgumentsSource(NumberArgumentProvider.class)
  void worksOkWithNumber(Object parameter, Object expectedResult, String parameterClassName) {
    // GIVEN
    var decr4 = PrimitiveFactory.DECR4();
    var decr4Addr = nextInt();
    var ip = anInstructionPointer().with(decr4Addr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(decr4Addr, decr4).build())
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(state1)
        .hasMemoryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().with(expectedResult).build())
        .hasReturnStackEqualTo(state1);
  }

  @DisplayName("should throw if top of parameter stack is not a number.")
  @ParameterizedTest(name = "{displayName} parameter(type={1})")
  @ArgumentsSource(NonNumberArgumentProvider.class)
  void throwsIfNonNumber(Object parameter, String parameterClassName) {
    // GIVEN
    var decr4 = PrimitiveFactory.DECR4();
    var decr4Addr = nextInt();
    var ip = anInstructionPointer().with(decr4Addr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(decr4Addr, decr4).build())
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }

  @DisplayName("should trhow if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var decr4 = PrimitiveFactory.DECR4();
    var decr4Addr = nextInt();
    var ip = anInstructionPointer().with(decr4Addr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(decr4Addr, decr4).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }

  //////////////////////////////////////////////////////////////////////////////
  static class NonNumberArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(new Object(), Object.class.toString()),
          Arguments.of(RandomStringUtils.random(10), String.class.toString()),
          Arguments.of(List.of(), List.class.toString()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  static class NumberArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          ofBigDecimal(),
          ofBigInteger(),
          ofByte(),
          ofDouble(),
          ofFloat(),
          ofInt(),
          ofLong(),
          ofShort());
    }

    private Arguments ofBigDecimal() {
      var n = RandomUtils.nextBigDecimal();
      var nMinusFour = n.subtract(BigDecimal.valueOf(4l));
      return Arguments.of(n, nMinusFour, n.getClass().toString());
    }

    private Arguments ofBigInteger() {
      var n = RandomUtils.nextBigInteger();
      var nMinusFour = n.subtract(BigInteger.valueOf(4l));
      return Arguments.of(n, nMinusFour, n.getClass().toString());
    }

    private Arguments ofByte() {
      var n = RandomUtils.nextByte();
      var nMinusFour = (byte) (n - 4);
      return Arguments.of(n, nMinusFour, n.getClass().toString());
    }

    private Arguments ofDouble() {
      var n = RandomUtils.nextDouble();
      var nMinusFour = n - 4d;
      return Arguments.of(n, nMinusFour, n.getClass().toString());
    }

    private Arguments ofFloat() {
      var n = RandomUtils.nextFloat();
      var nMinusFour = n - 4f;
      return Arguments.of(n, nMinusFour, n.getClass().toString());
    }

    private Arguments ofInt() {
      var n = RandomUtils.nextInt();
      var nMinusFour = n - 4;
      return Arguments.of(n, nMinusFour, n.getClass().toString());
    }

    private Arguments ofLong() {
      var n = RandomUtils.nextLong();
      var nMinusFour = n - 4l;
      return Arguments.of(n, nMinusFour, n.getClass().toString());
    }

    private Arguments ofShort() {
      var n = RandomUtils.nextShort();
      var nMinusFour = (short) (n - 4);
      return Arguments.of(n, nMinusFour, n.getClass().toString());
    }
  }
}
