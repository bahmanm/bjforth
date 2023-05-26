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
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FROMRITest {

  @DisplayName(
      "pushes the return stack element at the index stored on the top of parameter stack onto parameter stack, ie i -> a")
  @Test
  void worksOk() {
    // GIVEN
    var fromri = new FROMRI();
    var fromriAddr = nextInt();
    var ip = anInstructionPointer().with(fromriAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var objectToFetch = new Object();
    var indexToFetch = org.apache.commons.lang3.RandomUtils.nextInt(0, 5);
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(fromriAddr, fromri).build())
            .withParameterStack(aParameterStack().with(indexToFetch).build())
            .withReturnStack(
                aReturnStack()
                    .with(IntStream.range(0, indexToFetch).mapToObj(i -> new Object()).toList())
                    .with(objectToFetch)
                    .build())
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
        .hasParameterStackEqualTo(aParameterStack().with(objectToFetch).build())
        .hasReturnStackEqualTo(state1);
  }

  @DisplayName("throw if there are fewer elements on the return stack than (index+1)")
  @Test
  void throwIfFewerElements() {
    // GIVEN
    var fromri = new FROMRI();
    var fromriAddr = nextInt();
    var ip = anInstructionPointer().with(fromriAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var indexToFetch = org.apache.commons.lang3.RandomUtils.nextInt(0, 5);
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(fromriAddr, fromri).build())
            .withParameterStack(aParameterStack().with(indexToFetch).build())
            .withReturnStack(
                aReturnStack()
                    .with(IntStream.range(0, indexToFetch).mapToObj(i -> new Object()).toList())
                    .build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(IndexOutOfBoundsException.class);
    assertThat(state2)
        .isEqualTo(
            aMachineState().copyFrom(state1).withParameterStack(aParameterStack().build()).build());
  }

  @DisplayName("should throw if parameter stack top is not a number.")
  @Test
  void throwsIfNonNumber() {
    // GIVEN
    var fromri = new FROMRI();
    var fromriAddr = nextInt();
    var ip = anInstructionPointer().with(fromriAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(fromriAddr, fromri).build())
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

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var fromri = new FROMRI();
    var fromriAddr = nextInt();
    var ip = anInstructionPointer().with(fromriAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(fromriAddr, fromri).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = new Machine(state2);

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }
}
