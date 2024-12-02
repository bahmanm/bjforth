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
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BRANCHTest {
  @Test
  @DisplayName("Move NIP by the offset stored at NIP.")
  public void worksOk() {
    // GIVEN
    var BRANCHaddr = getPrimitiveAddress("BRANCH");
    var offset = nextInt();
    var nip = RandomUtils.insecure().randomInt(1000, 2000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(BRANCHaddr)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(nip, offset).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(nip).plus(offset).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(offset).plus(1).build())
        .hasMemoryEqualTo(referenceState)
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(referenceState);
  }
}
