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
import java.math.MathContext;
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
import org.junit.jupiter.params.provider.MethodSource;

class MODTest {

  @DisplayName(
      "remainder of the top element of stack by the 2nd top element, ie ab -> c where c=a%b.")
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
    var MODaddr = getPrimitiveAddress("MOD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MODaddr)
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

  @DisplayName(
      "should throw if divisor is 0 and dividend is a BigDecimal/BigInteger/long/int/short/byte")
  @ParameterizedTest(
      name = "{displayName} parameter1(type={2}, value={0}) parameter2(type={2}, value={1})")
  @MethodSource("zeroDivisorNoDoubleOrFloat")
  void shouldThrowIfDivisorZero(Object parameter1, Object parameter2, String parameter1ClassName) {
    // GIVEN
    var MODaddr = getPrimitiveAddress("MOD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MODaddr)
            .withParameterStack(aParameterStack().with(parameter2, parameter1).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(ArithmeticException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @DisplayName("should return NaN if divisor is 0 and dividend is a double/float")
  @ParameterizedTest(
      name =
          "{displayName} parameter1(type={3}, value={0}) parameter2(type={3}, value={1}) expected({2})")
  @MethodSource("zeroDivisorDoubleAndFloat")
  void shouldReturnInfinityIfDivisorZero(
      Object parameter1, Object parameter2, Object expectedResult, String parameter1ClassName) {
    // GIVEN
    var MODaddr = getPrimitiveAddress("MOD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MODaddr)
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
    var MODaddr = getPrimitiveAddress("MOD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MODaddr)
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
    var MODaddr = getPrimitiveAddress("MOD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MODaddr)
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
    var MODaddr = getPrimitiveAddress("MOD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MODaddr)
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
            () -> RandomUtils.nextDoubleExcluding(0d),
            () -> RandomUtils.nextFloatExcluding(0f),
            () -> RandomUtils.nextLongExcluding(0l),
            () -> RandomUtils.nextIntExcluding(0),
            () -> RandomUtils.nextShortExcluding((short) 0),
            () -> RandomUtils.nextByteExcluding((byte) 0));

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
        return a.doubleValue() % b.doubleValue();
      } else if (a instanceof Float || b instanceof Float) {
        return a.floatValue() % b.floatValue();
      } else if (a instanceof Long || b instanceof Long) {
        return a.longValue() % b.longValue();
      } else if (a instanceof Integer || b instanceof Integer) {
        return a.intValue() % b.intValue();
      } else if (a instanceof Short || b instanceof Short) {
        return a.shortValue() % b.shortValue();
      } else if (a instanceof Byte || b instanceof Byte) {
        return a.byteValue() % b.byteValue();
      } else {
        throw new RuntimeException("Unexpected type for a Number");
      }
    }

    private Arguments ofBigDecimal() {
      var n = RandomUtils.nextBigDecimal();
      var m = RandomUtils.nextBigDecimalExcluding(0d);
      return Arguments.of(
          n,
          m,
          n.remainder(m, MathContext.DECIMAL128),
          BigDecimal.class.getCanonicalName(),
          BigDecimal.class.getCanonicalName());
    }

    private Arguments ofBigInteger() {
      var n = RandomUtils.nextBigInteger();
      var m = RandomUtils.nextBigIntegerExcluding(0);
      return Arguments.of(
          n,
          m,
          n.remainder(m),
          BigInteger.class.getCanonicalName(),
          BigInteger.class.getCanonicalName());
    }
  }

  static Stream<Arguments> zeroDivisorNoDoubleOrFloat() {
    return Stream.of(
        Arguments.of(
            RandomUtils.nextBigDecimal(), BigDecimal.ZERO, BigDecimal.class.getCanonicalName()),
        Arguments.of(
            RandomUtils.nextBigInteger(), BigInteger.ZERO, BigInteger.class.getCanonicalName()),
        Arguments.of(RandomUtils.nextLong(), 0l, Long.class.getCanonicalName()),
        Arguments.of(RandomUtils.nextInt(), 0, Integer.class.getCanonicalName()),
        Arguments.of(RandomUtils.nextShort(), (short) 0, Short.class.getCanonicalName()),
        Arguments.of(RandomUtils.nextByte(), (byte) 0, Byte.class.getCanonicalName()));
  }

  static Stream<Arguments> zeroDivisorDoubleAndFloat() {
    return Stream.of(
        Arguments.of(RandomUtils.nextDouble(), 0d, Double.NaN, Double.class.getCanonicalName()),
        Arguments.of(RandomUtils.nextFloat(), 0f, Float.NaN, Float.class.getCanonicalName()));
  }
}
