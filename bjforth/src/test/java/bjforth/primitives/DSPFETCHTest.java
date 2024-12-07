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
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.MachineException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DSPFETCHTest {

  @DisplayName("pushes the parameter stack pointer on to parameter stack")
  @Test
  void worksOk() {
    // GIVEN
    var DSPFETCHaddr = getPrimitiveAddress("DSP@");
    var pointer = org.apache.commons.lang3.RandomUtils.nextInt(0, 5);
    var parameterStackElements =
        IntStream.range(0, pointer + 1).mapToObj(i -> new Object()).toList();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPFETCHaddr)
            .withNextInstructionPointer(DSPFETCHaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(parameterStackElements).build())
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
        .hasParameterStackEqualTo(aParameterStack().with(referenceState).with(pointer).build());
  }

  @DisplayName("should throw if parameter stack is empty. ")
  @Test
  void throwsIfEmpty() {
    // GIVEN
    var DSPFETCHaddr = getPrimitiveAddress("DSP@");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPFETCHaddr)
            .withNextInstructionPointer(DSPFETCHaddr + 1)
            .withMemory(aMemory().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
