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
import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.junit.jupiter.api.Assertions.*;

import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ZBRANCHTest {

  @Test
  @DisplayName("Move NIP by the offset stored at NIP in case of top of parameter stack is 0.")
  public void worksOkIfZero() {
    // GIVEN
    var ZBRANCHaddr = getPrimitiveAddress("0BRANCH");
    var offset = RandomUtils.insecure().randomInt(10, 200);
    var memoryAddress = RandomUtils.insecure().randomInt(1000, 1500);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(memoryAddress)
            .withNextInstructionPointer(memoryAddress + 1)
            .withMemory(
                aMemory().with(memoryAddress, ZBRANCHaddr).with(memoryAddress + 1, offset).build())
            .withParameterStack(aParameterStack().with(0).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step(2);

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(
            anInstructionPointer().with(referenceState).plus(offset).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(offset).build());
  }

  @Test
  @DisplayName("Skip over offset in case of top of parameter stack is not 0.")
  public void worksOkIfNotZero() {
    // GIVEN
    var ZBRANCHaddr = getPrimitiveAddress("0BRANCH");
    var offset = RandomUtils.insecure().randomInt(10, 200);
    var memoryAddress = RandomUtils.insecure().randomInt(1000, 1500);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(memoryAddress)
            .withNextInstructionPointer(memoryAddress + 1)
            .withMemory(
                aMemory().with(memoryAddress, ZBRANCHaddr).with(memoryAddress + 1, offset).build())
            .withParameterStack(aParameterStack().with(1).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step(2);

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(referenceState).plus(2).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build());
  }

  @Test
  @DisplayName("Should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    var ZBRANCHaddr = getPrimitiveAddress("0BRANCH");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ZBRANCHaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // THEN
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
