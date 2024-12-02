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
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.BootstrapUtils;
import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FETCHTest {

  @DisplayName(
      "pushes the value pointed to by the top of the parameter stack onto parameter stack, ie a->b")
  @Test
  void worksOk() {
    // GIVEN
    var FETCHaddr = BootstrapUtils.getPrimitiveAddress("@");
    var objectToFetch = new Object();
    var addrToFetch = RandomUtils.insecure().randomInt(1000, 2000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(FETCHaddr)
            .withNextInstructionPointer(FETCHaddr + 1)
            .withMemory(aMemory().with(addrToFetch, objectToFetch).build())
            .withParameterStack(aParameterStack().with(addrToFetch).build())
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
        .hasMemoryEqualTo(aMemory().with(referenceState).build())
        .hasParameterStackEqualTo(aParameterStack().with(objectToFetch).build())
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("should throw if parameter stack top is not a number.")
  @Test
  void throwsIfNonNumber() {
    // GIVEN
    var FETCHaddr = BootstrapUtils.getPrimitiveAddress("@");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(FETCHaddr)
            .withNextInstructionPointer(FETCHaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(new Object()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var FETCHaddr = BootstrapUtils.getPrimitiveAddress("@");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(FETCHaddr)
            .withNextInstructionPointer(FETCHaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
