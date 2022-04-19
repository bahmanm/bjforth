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
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.Machine;
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

  @ParameterizedTest(
      name =
          "duplicates top of stack if non-zero number, ie a -> aa.  parameter(type={1}, value={0})")
  @ArgumentsSource(NonZeroNumberArgumentProvider.class)
  void worksOkWithNonZero(Object parameter, String parameterClassName) {
    // GIVEN
    var qdup = new QDUP();
    var qdupAddr = nextInt();
    var ip = anInstructionPointer().with(qdupAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(qdupAddr, qdup).build())
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
        .hasParameterStackEqualTo(aParameterStack().with(parameter, parameter).build())
        .hasReturnStackEqualTo(state1);
  }

  @ParameterizedTest(
      name =
          "does not modify stack if top element is a zero number, ie a -> a.  parameter(type={1}, value={0})")
  @ArgumentsSource(ZeroNumberArgumentProvider.class)
  void worksOkWithZero(Object parameter, String parameterClassName) {
    // GIVEN
    var qdup = new QDUP();
    var qdupAddr = nextInt();
    var ip = anInstructionPointer().with(qdupAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(qdupAddr, qdup).build())
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
        .hasParameterStackEqualTo(aParameterStack().with(parameter).build())
        .hasReturnStackEqualTo(state1);
  }

  @ParameterizedTest(
      name = "should throw if top of parameter stack is not a number. parameter(type={1})")
  @ArgumentsSource(NonNumberArgumentProvider.class)
  <E extends Object> void throwsIfNonNumber(E parameter, String parameterClassName) {
    // GIVEN
    var qdup = new QDUP();
    var qdupAddr = nextInt();
    var ip = anInstructionPointer().with(qdupAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(qdupAddr, qdup).build())
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }

  @Test
  @DisplayName("should trhow if ParameterStack is already empty.")
  void throwIfEmpty() {
    // GIVEN
    var qdup = new QDUP();
    var qdupAddr = nextInt();
    var ip = anInstructionPointer().with(qdupAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(qdupAddr, qdup).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }

  static class NonNumberArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(new Object(), Object.class.toString()),
          Arguments.of(RandomStringUtils.random(10), String.class.toString()),
          Arguments.of(List.of(), List.class.toString()));
    }
  }

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
