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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class LTTest {

  @DisplayName(
      "pushes 1 if second top is less than top of parameter stack, 0 otherwise, ie ab -> c where c=0 if !a<b and c=1 if a<b.")
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
    var lt = new LT();
    var ltAddr = nextInt();
    var ip = anInstructionPointer().with(ltAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(ltAddr, lt).build())
            .withParameterStack(aParameterStack().with(parameter2, parameter1).build())
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

  @DisplayName("should throw if either of ParameterStack top 2 is not a number.")
  @ParameterizedTest(
      name = "{displayName} parameter1(type={2}, value={0}) parameter2(type={3}, value={1})")
  @ArgumentsSource(ObjectArgumentProvider.class)
  void throwIfNonNumber(
      Object parameter1,
      Object Parameter2,
      String parameter1ClassName,
      String parameter2ClassName) {
    // GIVEN
    var lt = new LT();
    var ltAddr = nextInt();
    var ip = anInstructionPointer().with(ltAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(ltAddr, lt).build())
            .withParameterStack(aParameterStack().with(new Object(), new Object()).build())
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
    var lt = new LT();
    var ltAddr = nextInt();
    var ip = anInstructionPointer().with(ltAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(ltAddr, lt).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }

  @DisplayName("should throw if ParameterStack has only 1 element.")
  @Test
  void throwIfOnlyOneElement() {
    // GIVEN
    var lt = new LT();
    var ltAddr = nextInt();
    var ip = anInstructionPointer().with(ltAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(ltAddr, lt).build())
            .withParameterStack(aParameterStack().with(RandomUtils.nextBigDecimal()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
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
      return Stream.of(
              ofBigDecimal(),
              ofBigInteger(),
              ofBigDecimalAndNumber(),
              ofBigIntegerAndNumber(),
              ofNumbers())
          .collect(Collectors.reducing(Stream.empty(), Stream::concat));
    }

    private Stream<Arguments> ofBigIntegerAndNumber() {
      return IntStream.range(1, 20)
          .mapToObj(_i -> RandomUtils.nextBigInteger())
          .flatMap(
              bi -> {
                var b = RandomUtils.nextByte();
                var s = RandomUtils.nextShort();
                var i = RandomUtils.nextInt();
                var l = RandomUtils.nextLong();
                return Stream.of(
                    Arguments.of(
                        bi,
                        b,
                        bi.compareTo(BigInteger.valueOf(b.longValue())) == -1 ? 1 : 0,
                        BigInteger.class.getCanonicalName(),
                        b.getClass().getCanonicalName()),
                    Arguments.of(
                        bi,
                        s,
                        bi.compareTo(BigInteger.valueOf(s.longValue())) == -1 ? 1 : 0,
                        BigInteger.class.getCanonicalName(),
                        s.getClass().getCanonicalName()),
                    Arguments.of(
                        bi,
                        i,
                        bi.compareTo(BigInteger.valueOf(i.longValue())) == -1 ? 1 : 0,
                        BigInteger.class.getCanonicalName(),
                        i.getClass().getCanonicalName()),
                    Arguments.of(
                        bi,
                        l,
                        bi.compareTo(BigInteger.valueOf(l.longValue())) == -1 ? 1 : 0,
                        BigInteger.class.getCanonicalName(),
                        l.getClass().getCanonicalName()));
              });
    }

    private Stream<Arguments> ofBigDecimalAndNumber() {
      return IntStream.range(1, 20)
          .mapToObj(_i -> RandomUtils.nextBigDecimal())
          .flatMap(
              bd -> {
                var b = RandomUtils.nextByte();
                var s = RandomUtils.nextShort();
                var i = RandomUtils.nextInt();
                var l = RandomUtils.nextLong();
                var f = RandomUtils.nextFloat();
                var d = RandomUtils.nextDouble();
                return Stream.of(
                    Arguments.of(
                        bd,
                        b,
                        bd.compareTo(BigDecimal.valueOf(b.doubleValue())) == -1 ? 1 : 0,
                        BigDecimal.class.getCanonicalName(),
                        b.getClass().getCanonicalName()),
                    Arguments.of(
                        bd,
                        s,
                        bd.compareTo(BigDecimal.valueOf(s.doubleValue())) == -1 ? 1 : 0,
                        BigDecimal.class.getCanonicalName(),
                        s.getClass().getCanonicalName()),
                    Arguments.of(
                        bd,
                        i,
                        bd.compareTo(BigDecimal.valueOf(i.doubleValue())) == -1 ? 1 : 0,
                        BigDecimal.class.getCanonicalName(),
                        i.getClass().getCanonicalName()),
                    Arguments.of(
                        bd,
                        l,
                        bd.compareTo(BigDecimal.valueOf(l.doubleValue())) == -1 ? 1 : 0,
                        BigDecimal.class.getCanonicalName(),
                        l.getClass().getCanonicalName()),
                    Arguments.of(
                        bd,
                        f,
                        bd.compareTo(BigDecimal.valueOf(f.doubleValue())) == -1 ? 1 : 0,
                        BigDecimal.class.getCanonicalName(),
                        f.getClass().getCanonicalName()),
                    Arguments.of(
                        bd,
                        d,
                        bd.compareTo(BigDecimal.valueOf(d)) == -1 ? 1 : 0,
                        BigDecimal.class.getCanonicalName(),
                        d.getClass().getCanonicalName()));
              });
    }

    private Stream<Arguments> ofBigDecimal() {
      var numbers = IntStream.range(1, 20).mapToObj(_i -> RandomUtils.nextBigDecimal()).toList();
      return numbers.stream()
          .flatMap(n -> numbers.stream().flatMap(m -> Stream.of(Pair.of(n, m), Pair.of(m, n))))
          .map(
              pair -> {
                var n = pair.getLeft();
                var m = pair.getRight();
                return Arguments.of(
                    n,
                    m,
                    n.compareTo(m) == -1 ? 1 : 0,
                    BigDecimal.class.getCanonicalName(),
                    BigDecimal.class.getCanonicalName());
              });
    }

    private Stream<Arguments> ofBigInteger() {
      var numbers = IntStream.range(1, 20).mapToObj(_i -> RandomUtils.nextBigInteger()).toList();
      return numbers.stream()
          .flatMap(n -> numbers.stream().flatMap(m -> Stream.of(Pair.of(n, m), Pair.of(m, n))))
          .map(
              pair -> {
                var n = pair.getLeft();
                var m = pair.getRight();
                return Arguments.of(
                    n,
                    m,
                    n.compareTo(m) == -1 ? 1 : 0,
                    BigInteger.class.getCanonicalName(),
                    BigInteger.class.getCanonicalName());
              });
    }

    private Stream<Arguments> ofNumbers() {
      var numbers =
          IntStream.range(0, 5)
              .mapToObj(_i -> numberGenerators.stream().map(Supplier::get))
              .collect(Collectors.reducing(Stream.empty(), Stream::concat))
              .toList();
      return numbers.stream()
          .flatMap(n -> numbers.stream().flatMap(m -> Stream.of(Pair.of(n, m))))
          .map(
              pair -> {
                var n = pair.getLeft();
                var m = pair.getRight();
                return Arguments.of(
                    n,
                    m,
                    ((Number) n).doubleValue() < ((Number) m).doubleValue() ? 1 : 0,
                    n.getClass().getCanonicalName(),
                    m.getClass().getCanonicalName());
              });
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  static class ObjectArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(
              new Object(),
              nextInt(),
              Object.class.getCanonicalName(),
              Integer.class.getCanonicalName()),
          Arguments.of(
              nextInt(),
              new Object(),
              Integer.class.getCanonicalName(),
              Object.class.getCanonicalName()),
          Arguments.of(
              new Object(),
              new Object(),
              Object.class.getCanonicalName(),
              Object.class.getCanonicalName()));
    }
  }
}
