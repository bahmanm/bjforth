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
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;

import bjforth.variables.Variables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LATESTTest {

  @Test
  @DisplayName("Push the value of LATEST to parameter stack.")
  public void worksOk() {
    // GIVEN
    var LATESTaddr = getPrimitiveAddress("LATEST");
    var latestValue = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(LATESTaddr)
            .withNextInstructionPointer(LATESTaddr + 1)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("LATEST").getAddress(), latestValue);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(referenceState).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(referenceState)
        .hasParameterStackEqualTo(aParameterStack().with(latestValue).build())
        .hasReturnStackEqualTo(referenceState);
  }
}
