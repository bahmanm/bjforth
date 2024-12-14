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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

class NEQUTest {

  @DisplayName(
      "pushes 1 if top 2 are not equal, 0 otherwise, ie ab -> c where c=0 if a==b and c=1 if a!=b.")
  @ParameterizedTest(
      name =
          "{displayName} parameter1(type={3}, value={0}) parameter2(type={4}, value={1}) expected({2})")
  @ArgumentsSource(ObjectArgumentProvider.class)
  void worksOkWithNumbers(
      Object parameter1,
      Object parameter2,
      Object expectedResult,
      String parameter1ClassName,
      String parameter2ClassName) {
    // GIVEN
    var NEQUaddr = getPrimitiveAddress("<>");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(NEQUaddr)
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

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var NEQUaddr = getPrimitiveAddress("<>");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(NEQUaddr)
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
    var NEQUaddr = getPrimitiveAddress("<>");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(NEQUaddr)
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
  static class ObjectArgumentProvider implements ArgumentsProvider {

    private static final List<Supplier<? extends Number>> numberGenerators =
        List.of(
            RandomUtils::nextDouble,
            RandomUtils::nextFloat,
            RandomUtils::nextLong,
            RandomUtils::nextInt,
            RandomUtils::nextShort,
            RandomUtils::nextByte,
            RandomUtils::nextBigDecimal,
            RandomUtils::nextBigInteger);

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(ofNumbers(), ofStrings(), ofObjects())
          .collect(Collectors.reducing(Stream.empty(), Stream::concat));
    }

    private Stream<Arguments> ofObjects() {
      var result = new ArrayList<Arguments>();
      var obj1 = new Object();
      var obj2 = new Object();
      result.addAll(
          List.of(
              Arguments.of(
                  obj1,
                  obj2,
                  obj1.equals(obj2) ? 0 : 1,
                  Object.class.getCanonicalName(),
                  Object.class.getCanonicalName()),
              Arguments.of(
                  obj1,
                  obj1,
                  obj1.equals(obj1) ? 0 : 1,
                  Object.class.getCanonicalName(),
                  Object.class.getCanonicalName())));
      try {
        var file1 =
            File.createTempFile(
                RandomStringUtils.randomAlphanumeric(3), RandomStringUtils.randomAlphanumeric(3));
        var file2 =
            File.createTempFile(
                RandomStringUtils.randomAlphanumeric(3), RandomStringUtils.randomAlphanumeric(3));
        result.addAll(
            List.of(
                Arguments.of(
                    file1,
                    file2,
                    file1.equals(file2) ? 0 : 1,
                    file1.getClass().getCanonicalName(),
                    file2.getClass().getCanonicalName()),
                Arguments.of(
                    file1,
                    file1,
                    file1.equals(file1) ? 0 : 1,
                    file1.getClass().getCanonicalName(),
                    file1.getClass().getCanonicalName()),
                Arguments.of(
                    file1,
                    obj1,
                    file1.equals(obj1) ? 0 : 1,
                    file1.getClass().getCanonicalName(),
                    obj1.getClass().getCanonicalName())));
      } catch (IOException e) {
      }
      return result.stream();
    }

    private Stream<Arguments> ofStrings() {
      var strings = IntStream.range(2, 20).mapToObj(RandomStringUtils::random).toList();
      return strings.stream()
          .flatMap(str1 -> strings.stream().flatMap(str2 -> Stream.of(Pair.of(str1, str2))))
          .map(
              strPair -> {
                var str1 = strPair.getLeft();
                var str2 = strPair.getRight();
                return Arguments.of(
                    str1,
                    str2,
                    str1.equals(str2) ? 0 : 1,
                    String.class.getCanonicalName(),
                    String.class.getCanonicalName());
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
              numberPair -> {
                var n = numberPair.getLeft();
                var m = numberPair.getRight();
                return Arguments.of(
                    n,
                    m,
                    n.equals(m) ? 0 : 1,
                    n.getClass().getCanonicalName(),
                    m.getClass().getCanonicalName());
              });
    }
  }
}
