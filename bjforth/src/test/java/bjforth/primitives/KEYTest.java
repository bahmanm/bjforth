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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bjforth.machine.MachineException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KEYTest {

  private final InputStream originalSystemIn = System.in;

  @AfterEach
  private void restoreSystemIn() {
    System.setIn(originalSystemIn);
  }

  @BeforeEach
  private void resetKEY() {
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
  }

  @DisplayName(
      "reads a single character from System.in and pushes the value on to parameter stack.")
  @Test
  void worksOk() {
    // GIVEN
    var str = RandomStringUtils.random(1);
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var key = PrimitiveFactory.KEY();
    var keyAddr = nextInt();
    var ip = anInstructionPointer().with(keyAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(keyAddr, key).build())
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
        .hasParameterStackEqualTo(aParameterStack().with(str.codePointAt(0)).build())
        .hasReturnStackEqualTo(state1);
  }

  @DisplayName("throws if reaches end of input stream.")
  @Test
  void throwsIfEndOfStream() {
    // GIVEN
    var str = "";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var key = PrimitiveFactory.KEY();
    var keyAddr = nextInt();
    var ip = anInstructionPointer().with(keyAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(keyAddr, key).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step)
        .isInstanceOf(MachineException.class)
        .hasMessage("End of stream");
    assertThat(state2).isEqualTo(state1);
  }
}
