/*
 * Copyright 2023 Bahman Movaqar
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
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;

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

    var word = PrimitiveFactory.WORD();
    var wordAddr = nextInt();
    var ip = anInstructionPointer().with(wordAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(wordAddr, word).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().with(wordStr).build())
        .hasReturnStackEqualTo(state1);
  }

  @DisplayName("skips leading space")
  @Test
  void skipsLeadingSpace() {
    // GIVEN
    var wordStr = RandomStringUtils.randomAlphanumeric(5);
    var str = " ".repeat(10) + wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var word = PrimitiveFactory.WORD();
    var wordAddr = nextInt();
    var ip = anInstructionPointer().with(wordAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(wordAddr, word).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().with(wordStr).build())
        .hasReturnStackEqualTo(state1);
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

    var word = PrimitiveFactory.WORD();
    var wordAddr = nextInt();
    var ip = anInstructionPointer().with(wordAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(wordAddr, word).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().with(wordStr).build())
        .hasReturnStackEqualTo(state1);
  }
}
