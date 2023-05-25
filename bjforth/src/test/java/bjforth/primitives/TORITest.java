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
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.util.ArrayList;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TORITest {

  @DisplayName(
      "stores the value (2nd top of stack) at the given index (top of stack) in the return stack, ie xi ->")
  @Test
  void worksOk() {
    // GIVEN
    var tori = new TORI();
    var toriAddr = nextInt();
    var ip = anInstructionPointer().with(toriAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var objectToStore = new Object();
    var indexToStore = org.apache.commons.lang3.RandomUtils.nextInt(0, 5);
    var state1ReturnStackElements =
        IntStream.range(0, indexToStore + 2).mapToObj(i -> new Object()).toList();
    var expectedReturnStackElements = new ArrayList<>(state1ReturnStackElements);
    expectedReturnStackElements.set(1, objectToStore);
    System.out.println(
        "objectToStore: "
            + objectToStore
            + ", expectedColl: "
            + expectedReturnStackElements.get(indexToStore)
            + ", initialColl: "
            + state1ReturnStackElements.get(indexToStore));
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(toriAddr, tori).build())
            .withParameterStack(aParameterStack().with(objectToStore, indexToStore).build())
            .withReturnStack(aReturnStack().with(state1ReturnStackElements).build())
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
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(aReturnStack().with(expectedReturnStackElements).build());
  }

  @DisplayName("should throw if parameter stack top is not a number.")
  @Test
  void throwsIfNonNumber() {
    // GIVEN
    var tori = new TORI();
    var toriAddr = nextInt();
    var ip = anInstructionPointer().with(toriAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(toriAddr, tori).build())
            .withParameterStack(aParameterStack().with(new Object(), new Object()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

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
    var tori = new TORI();
    var toriAddr = nextInt();
    var ip = anInstructionPointer().with(toriAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(toriAddr, tori).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }

  @DisplayName("should throw if ParameterStack has only 1 element.")
  @Test
  void throwIfOnlyOneElement() {
    // GIVEN
    var tori = new TORI();
    var toriAddr = nextInt();
    var ip = anInstructionPointer().with(toriAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(toriAddr, tori).build())
            .withParameterStack(aParameterStack().with(nextInt()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }
}
