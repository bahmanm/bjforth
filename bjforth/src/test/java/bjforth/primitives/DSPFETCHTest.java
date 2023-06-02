/*
 * Copyright 2023 Bahman Movaqar
 *
 * This file is part of BJForth.
 *
 * BJForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BJForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BJForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.primitives;

import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DSPFETCHTest {

  @DisplayName("pushes the parameter stack pointer on to parameter stack")
  @Test
  void worksOk() {
    // GIVEN
    var dspfetch = PrimitiveFactory.DSPFETCH();
    var dspfetchAddr = nextInt();
    var ip = anInstructionPointer().with(dspfetchAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var pointer = org.apache.commons.lang3.RandomUtils.nextInt(0, 5);
    var parameterStackElements =
        IntStream.range(0, pointer + 1).mapToObj(i -> new Object()).toList();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(dspfetchAddr, dspfetch).build())
            .withParameterStack(aParameterStack().with(parameterStackElements).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(state1)
        .hasMemoryEqualTo(aMemory().with(state1).build())
        .hasParameterStackEqualTo(aParameterStack().with(state1).with(pointer).build());
  }

  @DisplayName("should throw if parameter stack is empty. ")
  @Test
  void throwsIfEmpty() {
    // GIVEN
    var dspfetch = PrimitiveFactory.DSPFETCH();
    var dspfetchAddr = nextInt();
    var ip = anInstructionPointer().with(dspfetchAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(dspfetchAddr, dspfetch).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }
}
