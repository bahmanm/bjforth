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
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;
import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.Machine;
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

class ZLETest {

  @DisplayName(
      "pushes 1 if second top is less than 0, 0 otherwise, ie a -> c where c=1 if a<=0 and c=0 if a>0.")
  @ParameterizedTest(name = "{displayName} parameter(type={2}, value={0})")
  @ArgumentsSource(NumberArgumentProvider.class)
  void worksOkWithNumbers(Object parameter, Object expectedResult, String parameterClassName) {
    // GIVEN
    var zle = new ZLE();
    var zleAddr = nextInt();
    var ip = anInstructionPointer().with(zleAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(zleAddr, zle).build())
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

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

  @DisplayName("should throw if top of ParameterStack is not a number.")
  void throwIfNonNumber() {
    // GIVEN
    var zle = new ZLE();
    var zleAddr = nextInt();
    var ip = anInstructionPointer().with(zleAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(zleAddr, zle).build())
            .withParameterStack(aParameterStack().with(new Object()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var zle = new ZLE();
    var zleAddr = nextInt();
    var ip = anInstructionPointer().with(zleAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(zleAddr, zle).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }

  //////////////////////////////////////////////////////////////////////////////
  static class NumberArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(BigDecimal.ZERO, 1, BigDecimal.class.getCanonicalName()),
          Arguments.of(
              RandomUtils.nextBigDecimalExcluding(0).abs().negate(),
              1,
              BigDecimal.class.getCanonicalName()),
          Arguments.of(
              RandomUtils.nextBigDecimalExcluding(0).abs(), 0, BigDecimal.class.getCanonicalName()),
          Arguments.of(BigInteger.ZERO, 1, BigInteger.class.getCanonicalName()),
          Arguments.of(
              RandomUtils.nextBigIntegerExcluding(0).abs().negate(),
              1,
              BigInteger.class.getCanonicalName()),
          Arguments.of(
              RandomUtils.nextBigIntegerExcluding(0).abs(), 0, BigInteger.class.getCanonicalName()),
          Arguments.of(0d, 1, Double.class.getCanonicalName()),
          Arguments.of(
              -abs(RandomUtils.nextDoubleExcluding(0d)), 1, Double.class.getCanonicalName()),
          Arguments.of(
              abs(RandomUtils.nextDoubleExcluding(0d)), 0, Double.class.getCanonicalName()),
          Arguments.of(0f, 1, Float.class.getCanonicalName()),
          Arguments.of(-abs(RandomUtils.nextFloatExcluding(0f)), 1, Float.class.getCanonicalName()),
          Arguments.of(abs(RandomUtils.nextFloatExcluding(0f)), 0, Float.class.getCanonicalName()),
          Arguments.of(0l, 1, Long.class.getCanonicalName()),
          Arguments.of(-abs(RandomUtils.nextLongExcluding(0l)), 1, Long.class.getCanonicalName()),
          Arguments.of(abs(RandomUtils.nextLongExcluding(0l)), 0, Long.class.getCanonicalName()),
          Arguments.of(0, 1, Integer.class.getCanonicalName()),
          Arguments.of(-abs(RandomUtils.nextIntExcluding(0)), 1, Integer.class.getCanonicalName()),
          Arguments.of(abs(RandomUtils.nextIntExcluding(0)), 0, Integer.class.getCanonicalName()),
          Arguments.of((short) 0, 1, Short.class.getCanonicalName()),
          Arguments.of(
              -abs(RandomUtils.nextShortExcluding((short) 0)), 1, Short.class.getCanonicalName()),
          Arguments.of(
              abs(RandomUtils.nextShortExcluding((short) 0)), 0, Short.class.getCanonicalName()),
          Arguments.of((byte) 0, 1, Byte.class.getCanonicalName()),
          Arguments.of(
              -abs(RandomUtils.nextByteExcluding((byte) 0)), 1, Byte.class.getCanonicalName()),
          Arguments.of(
              abs(RandomUtils.nextByteExcluding((byte) 0)), 0, Byte.class.getCanonicalName()));
    }
  }
}
