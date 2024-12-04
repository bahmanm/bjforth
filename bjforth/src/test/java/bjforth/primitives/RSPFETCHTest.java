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

import static bjforth.machine.BootstrapUtils.getPrimitiveAddress;
import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RSPFETCHTest {

  @DisplayName("pushes the return stack pointer on to parameter stack")
  @Test
  void worksOk() {
    // GIVEN
    var RSPFETCHaddr = getPrimitiveAddress("RSP@");
    var pointer = org.apache.commons.lang3.RandomUtils.nextInt(0, 5);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RSPFETCHaddr)
            .withNextInstructionPointer(RSPFETCHaddr + 1)
            .withReturnStack(
                aReturnStack()
                    .with(IntStream.range(0, pointer + 1).mapToObj(i -> new Object()).toList())
                    .build())
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
        .hasParameterStackEqualTo(aParameterStack().with(pointer).build())
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("should throw if return stack is empty.")
  @Test
  void throwsIfEmpty() {
    // GIVEN
    var RSPFETCHaddr = getPrimitiveAddress("RSP@");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RSPFETCHaddr)
            .withNextInstructionPointer(RSPFETCHaddr)
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
