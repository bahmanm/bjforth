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

import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bjforth.machine.MachineException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EMITTest {

  private final PrintStream originalSystemOut = System.out;
  private ByteArrayOutputStream outputStream = null;
  private PrintStream systemOut = null;

  @AfterEach
  private void restoreSystemIn() {
    System.setOut(originalSystemOut);
    systemOut.close();
    systemOut = null;
    try {
      outputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      outputStream = null;
    }
  }

  @BeforeEach
  private void setupSystemOut() {
    outputStream = new ByteArrayOutputStream();
    systemOut = new PrintStream(outputStream);
    System.setOut(systemOut);
  }

  @DisplayName("writes the top of the parameter stack to System.out")
  @Test
  void worksOk() {
    // GIVEN
    var str = RandomStringUtils.random(1);
    var ch = str.codePointAt(0);

    var emit = PrimitiveFactory.EMIT();
    var emitAddr = nextInt();
    var ip = anInstructionPointer().with(emitAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(emitAddr, emit).build())
            .withParameterStack(aParameterStack().with(ch).build())
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
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(state1);
    assertThat(outputStream.toByteArray()).containsExactly(str.getBytes());
  }

  @DisplayName("should throw if parameter stack top is not an integer.")
  @Test
  void throwsIfNonNumber() {
    // GIVEN
    var emit = PrimitiveFactory.EMIT();
    var emitAddr = nextInt();
    var ip = anInstructionPointer().with(emitAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(emitAddr, emit).build())
            .withParameterStack(aParameterStack().with(new Object()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

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
    var emit = PrimitiveFactory.EMIT();
    var emitAddr = nextInt();
    var ip = anInstructionPointer().with(emitAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(emitAddr, emit).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }
}
