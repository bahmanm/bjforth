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

import static bjforth.machine.BootstrapUtils.getPrimitiveAddress;
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
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

class INCR4Test {

  @DisplayName("increments top of stack by 4, ie a -> a+4.")
  @ParameterizedTest(name = "{displayName} parameter(type={2}, value={0}, expected={1})")
  @ArgumentsSource(NumberArgumentProvider.class)
  void worksOkWithNumber(Object parameter, Object expectedResult, String parameterClassName) {
    // GIVEN
    var INCR4addr = getPrimitiveAddress("4+");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(INCR4addr)
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(expectedResult).build());
  }

  @DisplayName("should throw if top of parameter stack is not a number.")
  @ParameterizedTest(name = "{displayName} parameter(type={1})")
  @ArgumentsSource(NonNumberArgumentProvider.class)
  void throwsIfNonNumber(Object parameter, String parameterClassName) {
    // GIVEN
    var INCR4addr = getPrimitiveAddress("4+");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(INCR4addr)
            .withNextInstructionPointer(INCR4addr + 1)
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @DisplayName("should trhow if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var INCR4addr = getPrimitiveAddress("4+");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(INCR4addr)
            .withNextInstructionPointer(INCR4addr + 1)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
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
      var nPlusFour = n.add(BigDecimal.valueOf(4l));
      return Arguments.of(n, nPlusFour, n.getClass().toString());
    }

    private Arguments ofBigInteger() {
      var n = RandomUtils.nextBigInteger();
      var nPlusFour = n.add(BigInteger.valueOf(4l));
      return Arguments.of(n, nPlusFour, n.getClass().toString());
    }

    private Arguments ofByte() {
      var n = RandomUtils.nextByte();
      var nPlusFour = (byte) (n + 4);
      return Arguments.of(n, nPlusFour, n.getClass().toString());
    }

    private Arguments ofDouble() {
      var n = RandomUtils.nextDouble();
      var nPlusFour = n + 4d;
      return Arguments.of(n, nPlusFour, n.getClass().toString());
    }

    private Arguments ofFloat() {
      var n = RandomUtils.nextFloat();
      var nPlusFour = n + 4f;
      return Arguments.of(n, nPlusFour, n.getClass().toString());
    }

    private Arguments ofInt() {
      var n = RandomUtils.nextInt();
      var nPlusFour = n + 4;
      return Arguments.of(n, nPlusFour, n.getClass().toString());
    }

    private Arguments ofLong() {
      var n = RandomUtils.nextLong();
      var nPlusFour = n + 4l;
      return Arguments.of(n, nPlusFour, n.getClass().toString());
    }

    private Arguments ofShort() {
      var n = RandomUtils.nextShort();
      var nPlusFour = (short) (n + 4);
      return Arguments.of(n, nPlusFour, n.getClass().toString());
    }
  }
}
