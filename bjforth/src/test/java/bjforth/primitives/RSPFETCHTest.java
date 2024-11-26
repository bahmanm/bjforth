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

import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static bjforth.utils.RandomUtils.nextInt;
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
    var rspfetch = PrimitiveFactory.RSPFETCH();
    var rspfetchAddr = nextInt();
    var ip = anInstructionPointer().with(rspfetchAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var pointer = org.apache.commons.lang3.RandomUtils.nextInt(0, 5);
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(rspfetchAddr, rspfetch).build())
            .withReturnStack(
                aReturnStack()
                    .with(IntStream.range(0, pointer + 1).mapToObj(i -> new Object()).toList())
                    .build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(state1)
        .hasMemoryEqualTo(aMemory().with(state1).build())
        .hasParameterStackEqualTo(aParameterStack().with(pointer).build())
        .hasReturnStackEqualTo(state1);
  }

  @DisplayName("should throw if return stack is empty.")
  @Test
  void throwsIfEmpty() {
    // GIVEN
    var rspfetch = PrimitiveFactory.RSPFETCH();
    var rspfetchAddr = nextInt();
    var ip = anInstructionPointer().with(rspfetchAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(rspfetchAddr, rspfetch).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
  }
}
