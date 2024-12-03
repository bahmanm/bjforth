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
import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
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

class QDUPTest {

  @DisplayName("duplicates top of stack if non-zero number, ie a -> aa.")
  @ParameterizedTest(name = "{displayName} parameter(type={1}, value={0})")
  @ArgumentsSource(NonZeroNumberArgumentProvider.class)
  void worksOkWithNonZero(Object parameter, String parameterClassName) {
    // GIVEN
    var QDUPaddr = getPrimitiveAddress("?DUP");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QDUPaddr)
            .withNextInstructionPointer(QDUPaddr + 1)
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(referenceState).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(referenceState)
        .hasParameterStackEqualTo(aParameterStack().with(parameter, parameter).build())
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("does not modify stack if top element is a zero number, ie a -> a.")
  @ParameterizedTest(name = "{displayName} parameter(type={1}, value={0})")
  @ArgumentsSource(ZeroNumberArgumentProvider.class)
  void worksOkWithZero(Object parameter, String parameterClassName) {
    // GIVEN
    var QDUPaddr = getPrimitiveAddress("?DUP");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QDUPaddr)
            .withNextInstructionPointer(QDUPaddr + 1)
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(referenceState).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(referenceState)
        .hasParameterStackEqualTo(aParameterStack().with(parameter).build())
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("should throw if top of parameter stack is not a number.")
  @ParameterizedTest(name = "{displayName} parameter(type={1})")
  @ArgumentsSource(NonNumberArgumentProvider.class)
  <E extends Object> void throwsIfNonNumber(E parameter, String parameterClassName) {
    // GIVEN
    var QDUPaddr = getPrimitiveAddress("?DUP");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QDUPaddr)
            .withNextInstructionPointer(QDUPaddr + 1)
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
    var QDUPaddr = getPrimitiveAddress("?DUP");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QDUPaddr)
            .withNextInstructionPointer(QDUPaddr + 1)
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
  static class ZeroNumberArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(BigDecimal.valueOf(0l), BigDecimal.class.toString()),
          Arguments.of(BigInteger.valueOf(0l), BigInteger.class.toString()),
          Arguments.of(Byte.valueOf("0"), Byte.class.toString()),
          Arguments.of(Double.valueOf("0"), Double.class.toString()),
          Arguments.of(Float.valueOf("0"), Float.class.toString()),
          Arguments.of(Integer.valueOf("0"), Integer.class.toString()),
          Arguments.of(Long.valueOf("0"), Long.class.toString()),
          Arguments.of(Short.valueOf("0"), Short.class.toString()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  static class NonZeroNumberArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(RandomUtils.nextBigDecimalExcluding(0), BigDecimal.class.toString()),
          Arguments.of(RandomUtils.nextBigIntegerExcluding(0), BigInteger.class.toString()),
          Arguments.of(RandomUtils.nextByteExcluding((byte) 0), Byte.class.toString()),
          Arguments.of(RandomUtils.nextDoubleExcluding(0d), Double.class.toString()),
          Arguments.of(RandomUtils.nextFloatExcluding(0f), Float.class.toString()),
          Arguments.of(RandomUtils.nextIntExcluding(0), Integer.class.toString()),
          Arguments.of(RandomUtils.nextLongExcluding(0l), Long.class.toString()),
          Arguments.of(RandomUtils.nextShortExcluding((short) 0), Short.class.toString()));
    }
  }
}
