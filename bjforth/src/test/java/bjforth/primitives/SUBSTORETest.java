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
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.MachineException;
import bjforth.utils.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SUBSTORETest {

  @DisplayName("decrement the value stored in momory by a given amount, ie va -> ")
  @Test
  void worksOk() {
    // GIVEN
    var substore = PrimitiveFactory.SUBSTORE();
    var substoreAddr = nextInt();
    var ip = anInstructionPointer().with(substoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var addrToSubstore = RandomUtils.nextIntExcluding(substoreAddr);
    var initialValue = nextInt();
    var decrement = nextInt();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(
                aMemory().with(substoreAddr, substore).with(addrToSubstore, initialValue).build())
            .withParameterStack(aParameterStack().with(decrement, addrToSubstore).build())
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
        .hasMemoryEqualTo(
            aMemory().with(state1).with(addrToSubstore, initialValue - decrement).build())
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(state1);
  }

  @DisplayName("should throw if parameter stack top is not a number.")
  @Test
  void throwsIfNonNumber() {
    // GIVEN
    var substore = PrimitiveFactory.SUBSTORE();
    var substoreAddr = nextInt();
    var ip = anInstructionPointer().with(substoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(substoreAddr, substore).build())
            .withParameterStack(aParameterStack().with(new Object()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var substore = PrimitiveFactory.SUBSTORE();
    var substoreAddr = nextInt();
    var ip = anInstructionPointer().with(substoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(substoreAddr, substore).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }
}
