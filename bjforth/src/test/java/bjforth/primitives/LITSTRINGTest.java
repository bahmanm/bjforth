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

import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.BootstrapUtils;
import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LITSTRINGTest {

  @DisplayName("pushes the address of string onto parameter stack and skips over the string.")
  @Test
  void worksOk() {
    // GIVEN
    var LITSTRINGaddr = BootstrapUtils.getPrimitiveAddress("LITSTRING");
    var aString = RandomStringUtils.insecure().next(10);
    var memoryAddress = RandomUtils.insecure().randomInt(1000, 2000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(memoryAddress)
            .withNextInstructionPointer(memoryAddress + 1)
            .withMemory(
                aMemory()
                    .with(memoryAddress, LITSTRINGaddr)
                    .with(memoryAddress + 1, aString)
                    .build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step(2);

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(memoryAddress + 1).build());
  }

  @DisplayName("throws an exception in case NIP doesn't point to a string.")
  @Test
  void throwIfNoString() {
    // GIVEN
    var LITSTRINGaddr = BootstrapUtils.getPrimitiveAddress("LITSTRING");
    var invalidData = nextInt();
    var memoryAddress = RandomUtils.insecure().randomInt(1000, 2000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(memoryAddress)
            .withNextInstructionPointer(memoryAddress + 1)
            .withMemory(
                aMemory()
                    .with(memoryAddress, LITSTRINGaddr)
                    .with(memoryAddress + 1, invalidData)
                    .build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(
        MachineException.class,
        () -> {
          machine.step(2);
        });
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(LITSTRINGaddr).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(memoryAddress).plus(1).build());
  }
}
