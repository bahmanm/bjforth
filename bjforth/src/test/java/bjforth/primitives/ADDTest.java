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
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.MachineException;
import bjforth.utils.RandomUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ADDTest {

  @DisplayName("sums the top 2 elements of stack, ie ab -> c where c=a+b.")
  @ParameterizedTest(
      name =
          "{displayName} parameter1(type={3}, value={0}) parameter2(type={4}, value={1}) expected({2})")
  @ArgumentsSource(NumberArgumentProvider.class)
  void worksOkWithNumbers(
      Object parameter1,
      Object parameter2,
      Object expectedResult,
      String parameter1ClassName,
      String parameter2ClassName) {
    // GIVEN
    var ADDaddr = getPrimitiveAddress("+");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ADDaddr)
            .withNextInstructionPointer(ADDaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(parameter2, parameter1).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(expectedResult).build());
  }

  @DisplayName("should throw if any of parameter stack top is not a number.")
  @ParameterizedTest(name = "{displayName} parameter1(type={2}) parameter2(type={3})")
  @ArgumentsSource(NonNumberArgumentProvider.class)
  void throwsIfNonNumber(
      Object parameter1,
      Object parameter2,
      String parameter1ClassName,
      String parameter2ClassName) {
    // GIVEN
    var ADDaddr = getPrimitiveAddress("+");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ADDaddr)
            .withNextInstructionPointer(ADDaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(parameter2, parameter1).build())
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
    var ADDaddr = getPrimitiveAddress("+");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ADDaddr)
            .withNextInstructionPointer(ADDaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }

  @DisplayName("should throw if ParameterStack has only 1 element.")
  @Test
  void throwIfOnlyOneElement() {
    // GIVEN
    var ADDaddr = getPrimitiveAddress("+");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ADDaddr)
            .withNextInstructionPointer(ADDaddr)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(RandomUtils.nextBigDecimal()).build())
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

  //////////////////////////////////////////////////////////////////////////////
  static class NonNumberArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(
              new Object(),
              new Object(),
              Object.class.getCanonicalName(),
              Object.class.getCanonicalName()),
          Arguments.of(
              RandomStringUtils.random(10),
              List.of(),
              String.class.getCanonicalName(),
              List.class.getCanonicalName()),
          Arguments.of(
              BigDecimal.ZERO,
              new Object(),
              BigDecimal.class.getCanonicalName(),
              Object.class.getCanonicalName()),
          Arguments.of(
              new Object(), 15, Object.class.getCanonicalName(), Integer.class.getCanonicalName()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  static class NumberArgumentProvider implements ArgumentsProvider {

    private static final List<Supplier<? extends Number>> numberGenerators =
        List.of(
            RandomUtils::nextDouble,
            RandomUtils::nextFloat,
            RandomUtils::nextLong,
            RandomUtils::nextInt,
            RandomUtils::nextShort,
            RandomUtils::nextByte);

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.concat(Stream.of(ofBigDecimal(), ofBigInteger()), ofBoxedPrimitives());
    }

    private Stream<Arguments> ofBoxedPrimitives() {
      return numberGenerators.stream()
          .flatMap(
              gen1 ->
                  numberGenerators.stream()
                      .flatMap(gen2 -> Stream.of(Pair.of(gen1, gen2), Pair.of(gen2, gen1))))
          .map(genPair -> Pair.of(genPair.getLeft().get(), genPair.getRight().get()))
          .map(
              numberPair -> {
                var n = numberPair.getLeft();
                var m = numberPair.getRight();
                return Arguments.of(
                    n,
                    m,
                    sum(n, m),
                    n.getClass().getCanonicalName(),
                    m.getClass().getCanonicalName());
              });
    }

    private Number sum(Number a, Number b) {
      if (a instanceof Double || b instanceof Double) {
        return a.doubleValue() + b.doubleValue();
      } else if (a instanceof Float || b instanceof Float) {
        return a.floatValue() + b.floatValue();
      } else if (a instanceof Long || b instanceof Long) {
        return a.longValue() + b.longValue();
      } else if (a instanceof Integer || b instanceof Integer) {
        return a.intValue() + b.intValue();
      } else if (a instanceof Short || b instanceof Short) {
        return a.shortValue() + b.shortValue();
      } else if (a instanceof Byte || b instanceof Byte) {
        return a.byteValue() + b.byteValue();
      } else {
        throw new RuntimeException("Unexpected type for a Number");
      }
    }

    private Arguments ofBigDecimal() {
      var n = RandomUtils.nextBigDecimal();
      var m = RandomUtils.nextBigDecimal();
      return Arguments.of(
          n, m, n.add(m), BigDecimal.class.getCanonicalName(), BigDecimal.class.getCanonicalName());
    }

    private Arguments ofBigInteger() {
      var n = RandomUtils.nextBigInteger();
      var m = RandomUtils.nextBigInteger();
      return Arguments.of(
          n, m, n.add(m), BigInteger.class.getCanonicalName(), BigInteger.class.getCanonicalName());
    }
  }
}
