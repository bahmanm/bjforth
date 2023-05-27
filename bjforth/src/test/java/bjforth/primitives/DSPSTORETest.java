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
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DSPSTORETest {

  @DisplayName("sets the paramewter stack pointer to the value at the top of parameter stack")
  @Test
  void worksOk() {
    // GIVEN
    var dspstore = new DSPSTORE();
    var dspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(dspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var pointerToStore = RandomUtils.nextInt(0, 5);
    var state1ParameterStackElements =
        IntStream.range(0, pointerToStore + 5).mapToObj(i -> new Object()).toList();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(dspstoreAddr, dspstore).build())
            .withParameterStack(
                aParameterStack().with(state1ParameterStackElements).with(pointerToStore).build())
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
        .hasParameterStackPointerEqualTo(pointerToStore)
        .hasParameterStackEqualTo(
            aParameterStack()
                .with(state1ParameterStackElements.subList(0, pointerToStore + 1))
                .build());
  }

  @DisplayName("should throw if parameter stack is empty.")
  @Test
  void throwsIfParameterStackEmpty() {
    // GIVEN
    var dspstore = new DSPSTORE();
    var dspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(dspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(dspstoreAddr, dspstore).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(state1);
  }

  @DisplayName("should throw if parameter stack top is not a number.")
  @Test
  void throwsIfParameterStackNonNumber() {
    // GIVEN
    var dspstore = new DSPSTORE();
    var dspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(dspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(dspstoreAddr, dspstore).build())
            .withParameterStack(aParameterStack().with(new Object()).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }

  @DisplayName("should throw if pointer is beyond parameter stack size.")
  @Test
  void throwsIfPointerTooLarge() {
    // GIVEN
    var dspstore = new DSPSTORE();
    var dspstoreAddr = nextInt();
    var ip = anInstructionPointer().with(dspstoreAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var pointer = RandomUtils.nextInt(5, 10);
    var parameterStackElements = List.of(nextInt(), nextInt());
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(dspstoreAddr, dspstore).build())
            .withParameterStack(
                aParameterStack().with(parameterStackElements).with(pointer).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2)
        .isEqualTo(
            aMachineState()
                .copyFrom(state1)
                .withParameterStack(aParameterStack().with(parameterStackElements).build())
                .build());
  }
}
