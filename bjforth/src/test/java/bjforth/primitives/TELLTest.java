/*
 * Copyright 2024 Bahman Movaqar
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
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import java.io.ByteArrayOutputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TELLTest {

  @Test
  @DisplayName("Writes a string to an output stream")
  public void worksOk() {
    // GIVEN
    var TELLaddr = getPrimitiveAddress("TELL");
    var stream = new ByteArrayOutputStream();
    var string = RandomStringUtils.secure().next(10);
    var length = string.length();
    var stringAddr = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(TELLaddr)
            .withMemory(aMemory().with(stringAddr, string).build())
            .withParameterStack(aParameterStack().with(length, stringAddr, stream).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().build());
    assertThat(stream.toByteArray()).isEqualTo(string.getBytes());
  }

  @Test
  @DisplayName("should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var TELLaddr = getPrimitiveAddress("TELL");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(TELLaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }

  @Test
  @DisplayName("should throw if ParameterStack has only 1 element.")
  public void throwIfOneElement() {
    // GIVEN
    var TELLaddr = getPrimitiveAddress("TELL");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(TELLaddr)
            .withParameterStack(aParameterStack().with(nextInt()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @Test
  @DisplayName("should throw if ParameterStack has only 2 elements.")
  public void throwIfTwoElements() {
    // GIVEN
    var TELLaddr = getPrimitiveAddress("TELL");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(TELLaddr)
            .withParameterStack(aParameterStack().with(nextInt(), nextInt()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }
}
