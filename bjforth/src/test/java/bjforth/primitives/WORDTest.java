/*
 * Copyright 2023 Bahman Movaqar
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
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WORDTest {

  private final InputStream originalSystemIn = System.in;

  @AfterEach
  private void restoreSystemIn() {
    System.setIn(originalSystemIn);
  }

  @BeforeEach
  private void resetPrimitives() {
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
  }

  @DisplayName("reads a word from stdin and pushes it on to parameter stack.")
  @Test
  void worksOk() {
    // GIVEN
    var wordStr = RandomStringUtils.randomAlphanumeric(10);
    var str = wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var WORDaddr = getPrimitiveAddress("WORD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(WORDaddr)
            .withNextInstructionPointer(WORDaddr + 1)
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
        .hasParameterStackEqualTo(aParameterStack().with(wordStr).build())
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("skips leading space")
  @Test
  void skipsLeadingSpace() {
    // GIVEN
    var wordStr = RandomStringUtils.randomAlphanumeric(5);
    var str = " ".repeat(10) + wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var WORDaddr = getPrimitiveAddress("WORD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(WORDaddr)
            .withNextInstructionPointer(WORDaddr + 1)
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
        .hasParameterStackEqualTo(aParameterStack().with(wordStr).build())
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("skips comment lines")
  @Test
  void skipsComments() {
    // GIVEN
    var wordStr = RandomStringUtils.randomAlphanumeric(5);
    var str =
        new StringBuilder()
            .append(" ".repeat(10))
            .append("\\" + RandomStringUtils.random(100) + "\n")
            .append("\n")
            .append("\\" + RandomStringUtils.random(200) + "\n")
            .append(wordStr + " ")
            .toString();
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var WORDaddr = getPrimitiveAddress("WORD");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(WORDaddr)
            .withNextInstructionPointer(WORDaddr + 1)
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
        .hasParameterStackEqualTo(aParameterStack().with(wordStr).build())
        .hasReturnStackEqualTo(referenceState);
  }
}
