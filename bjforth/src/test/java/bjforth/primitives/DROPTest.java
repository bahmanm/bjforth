/*
 * Copyright 2022 Bahman Movaqar
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
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DROPTest {

  @Test
  @DisplayName("drops the top of ParameterStack.")
  public void workOk() {
    // GIVEN
    var drop = new DROP();
    var dropAddr = nextInt();
    var ip = anInstructionPointer().with(dropAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 = aMachineState().withInstrcutionPointer(ip).withNextInstructionPointer(nip)
        .withMemory(aMemory().with(dropAddr, drop).build())
        .withParameterStack(aParameterStack().with(new Object()).build()).build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(state1).hasMemoryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().build()).hasReturnStackEqualTo(state1);
  }

  @Test
  @DisplayName("should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var drop = new DROP();
    var dropAddr = nextInt();
    var ip = anInstructionPointer().with(dropAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 = aMachineState().withInstrcutionPointer(ip).withNextInstructionPointer(nip)
        .withMemory(aMemory().with(dropAddr, drop).build())
        .withParameterStack(aParameterStack().build()).build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2).isEqualTo(state1);
  }
}
