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

class SWAPTest {

  @Test
  @DisplayName("swaps the two top elements of the ParameterStack.")
  public void worksOk() {
    // GIVEN
    var swap = new SWAP();
    var swapAddr = nextInt();
    var ip = anInstructionPointer().with(swapAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var parameter1 = nextInt();
    var parameter2 = nextInt();
    var state1 = aMachineState().withInstrcutionPointer(ip).withNextInstructionPointer(nip)
        .withMemory(aMemory().with(swapAddr, swap).build())
        .withParameterStack(aParameterStack().with(parameter1, parameter2).build()).build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(state1).hasMemoryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().with(parameter2, parameter1).build())
        .hasReturnStackEqualTo(state1);
  }

  @Test
  @DisplayName("should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var swap = new SWAP();
    var swapAddr = nextInt();
    var ip = anInstructionPointer().with(swapAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 = aMachineState().withInstrcutionPointer(ip).withNextInstructionPointer(nip)
        .withMemory(aMemory().with(swapAddr, swap).build())
        .withParameterStack(aParameterStack().build()).build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }

  @Test
  @DisplayName("should throw if ParameterStack has only 1 element.")
  public void throwIfOneElement() {
    // GIVEN
    var swap = new SWAP();
    var swapAddr = nextInt();
    var ip = anInstructionPointer().with(swapAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 = aMachineState().withInstrcutionPointer(ip).withNextInstructionPointer(nip)
        .withMemory(aMemory().with(swapAddr, swap).build())
        .withParameterStack(aParameterStack().with(nextInt()).build()).build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThrows(MachineException.class, machine::step);
  }

  @Test
  @DisplayName("should drop the top of ParameterStack if it has only 1 element.")
  public void dropIfOneElement() {
    // GIVEN
    var swap = new SWAP();
    var swapAddr = nextInt();
    var ip = anInstructionPointer().with(swapAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 = aMachineState().withInstrcutionPointer(ip).withNextInstructionPointer(nip)
        .withMemory(aMemory().with(swapAddr, swap).build())
        .withParameterStack(aParameterStack().with(nextInt()).build()).build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2).isEqualTo(
        aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }
}
