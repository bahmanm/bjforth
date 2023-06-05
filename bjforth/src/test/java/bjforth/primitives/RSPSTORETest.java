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
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RSPSTORETest {

  @DisplayName("sets the return stack pointer to the value at the top of parameter stack")
  @Test
  void worksOk() {
    // GIVEN
    var rspstore = PrimitiveFactory.RSPSTORE();
    var rspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(rspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var pointerToStore = RandomUtils.nextInt(0, 5);
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(rspstoreAddr, rspstore).build())
            .withParameterStack(aParameterStack().with(pointerToStore).build())
            .withReturnStack(
                aReturnStack()
                    .with(
                        IntStream.range(0, pointerToStore + 5).mapToObj(i -> new Object()).toList())
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
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackPointerEqualTo(pointerToStore);
  }

  @DisplayName("should throw if parameter stack is empty.")
  @Test
  void throwsIfParameterStackEmpty() {
    // GIVEN
    var rspstore = PrimitiveFactory.RSPSTORE();
    var rspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(rspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(rspstoreAddr, rspstore).build())
            .withReturnStack(aReturnStack().with(nextInt()).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(state1);
  }

  @DisplayName("should throw if parameter stack top is not a number.")
  @Test
  void throwsIfParameterStackNonNumber() {
    // GIVEN
    var rspstore = PrimitiveFactory.RSPSTORE();
    var rspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(rspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(rspstoreAddr, rspstore).build())
            .withParameterStack(aParameterStack().with(new Object()).build())
            .withReturnStack(aReturnStack().with(nextInt()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }

  @DisplayName("should throw if return stack is empty.")
  @Test
  void throwsIfReturnStackEmpty() {
    // GIVEN
    var rspstore = PrimitiveFactory.RSPSTORE();
    var rspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(rspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(rspstoreAddr, rspstore).build())
            .withParameterStack(aParameterStack().with(nextInt()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }

  @DisplayName("should throw if pointer is beyond return stack size.")
  @Test
  void throwsIfPointerTooLarge() {
    // GIVEN
    var rspstore = PrimitiveFactory.RSPSTORE();
    var rspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(rspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var pointer = RandomUtils.nextInt(5, 10);
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(rspstoreAddr, rspstore).build())
            .withParameterStack(aParameterStack().with(pointer).build())
            .withReturnStack(aReturnStack().with(nextInt(), nextInt()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }
}
