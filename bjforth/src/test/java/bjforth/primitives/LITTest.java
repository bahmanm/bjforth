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
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.*;

import bjforth.utils.RandomUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class LITTest {

  @DisplayName("should place the literal on stack and increase `nextInstructionPointer` by 2")
  @ParameterizedTest(name = "{displayName} literal(type={1}, value={0})")
  @ArgumentsSource(LiteralProvider.class)
  void worksOk(Object literal, String literalClassName) {
    // GIVEN
    var LITaddr = getPrimitiveAddress("LIT");
    var memoryAddress = org.apache.commons.lang3.RandomUtils.insecure().randomInt(1000, 2000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(memoryAddress)
            .withNextInstructionPointer(memoryAddress + 1)
            .withMemory(
                aMemory().with(memoryAddress, LITaddr).with(memoryAddress + 1, literal).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step(2);

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(memoryAddress).plus(2).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(memoryAddress).plus(3).build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(referenceState)
        .hasParameterStackEqualTo(aParameterStack().with(literal).build())
        .hasReturnStackEqualTo(referenceState);
  }

  //////////////////////////////////////////////////////////////////////////////

  static class LiteralProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(RandomUtils.nextBigDecimal(), BigDecimal.class.getCanonicalName()),
          Arguments.of(RandomUtils.nextBigInteger(), BigInteger.class.getCanonicalName()),
          Arguments.of(RandomUtils.nextLong(), Long.class.getCanonicalName()),
          Arguments.of(RandomStringUtils.random(20), String.class.getCanonicalName()),
          Arguments.of(new Object(), Object.class.getCanonicalName()));
    }
  }
}
