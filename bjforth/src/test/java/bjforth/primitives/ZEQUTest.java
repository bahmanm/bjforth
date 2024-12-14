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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ZEQUTest {

  @DisplayName(
      "pushes 1 if second top is 0, 0 otherwise, ie a -> c where c=1 if a==0 and c=0 if a!=0.")
  @ParameterizedTest(name = "{displayName} parameter(type={2}, value={0})")
  @ArgumentsSource(NumberArgumentProvider.class)
  void worksOkWithNumbers(Object parameter, Object expectedResult, String parameterClassName) {
    // GIVEN
    var ZEQUaddr = getPrimitiveAddress("0=");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ZEQUaddr)
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

  @DisplayName("should throw if top of ParameterStack is not a number.")
  @Test
  void throwIfNonNumber() {
    // GIVEN
    var ZEQUaddr = getPrimitiveAddress("0=");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ZEQUaddr)
            .withParameterStack(aParameterStack().with(new Object()).build())
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

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var ZEQUaddr = getPrimitiveAddress("0=");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ZEQUaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }

  //////////////////////////////////////////////////////////////////////////////
  static class NumberArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(BigDecimal.ZERO, 1, BigDecimal.class.getCanonicalName()),
          Arguments.of(
              RandomUtils.nextBigDecimalExcluding(0), 0, BigDecimal.class.getCanonicalName()),
          Arguments.of(BigInteger.ZERO, 1, BigInteger.class.getCanonicalName()),
          Arguments.of(
              RandomUtils.nextBigIntegerExcluding(0), 0, BigInteger.class.getCanonicalName()),
          Arguments.of(0d, 1, Double.class.getCanonicalName()),
          Arguments.of(RandomUtils.nextDoubleExcluding(0d), 0, Double.class.getCanonicalName()),
          Arguments.of(0f, 1, Float.class.getCanonicalName()),
          Arguments.of(RandomUtils.nextFloatExcluding(0f), 0, Float.class.getCanonicalName()),
          Arguments.of(0l, 1, Long.class.getCanonicalName()),
          Arguments.of(RandomUtils.nextLongExcluding(0l), 0, Long.class.getCanonicalName()),
          Arguments.of(0, 1, Integer.class.getCanonicalName()),
          Arguments.of(RandomUtils.nextIntExcluding(0), 0, Integer.class.getCanonicalName()),
          Arguments.of((short) 0, 1, Short.class.getCanonicalName()),
          Arguments.of(
              RandomUtils.nextShortExcluding((short) 0), 0, Short.class.getCanonicalName()),
          Arguments.of((byte) 0, 1, Byte.class.getCanonicalName()),
          Arguments.of(RandomUtils.nextByteExcluding((byte) 0), 0, Byte.class.getCanonicalName()));
    }
  }
}
