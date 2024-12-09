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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import bjforth.machine.MachineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EXECUTETest {

  @DisplayName("Jumps to the XT at the top of ParameterStack.")
  @Test
  void worksOk() {
    // GIVEN
    var EXECUTEaddr = getPrimitiveAddress("EXECUTE");
    var ADDaddr = getPrimitiveAddress("+");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(EXECUTEaddr)
            .withNextInstructionPointer(EXECUTEaddr + 1)
            .withParameterStack(aParameterStack().with(ADDaddr).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(ADDaddr).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(referenceState).build())
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(referenceState);
  }

  @DisplayName("Should throw if ParameterStack is already empty.")
  @Test
  void throwIfParameterStackEmpty() {
    // GIVEN
    var EXECUTEaddr = getPrimitiveAddress("EXECUTE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(EXECUTEaddr)
            .withNextInstructionPointer(EXECUTEaddr + 1)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
