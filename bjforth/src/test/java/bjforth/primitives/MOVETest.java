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

import bjforth.machine.BootstrapUtils;
import bjforth.machine.MachineException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MOVETest {

  @DisplayName(
      "copies a block of cells starting at address pointed to by the 2nd top of the stack and the number of cells at the top of the stack to the addresses starting at 3rd top of the stack, ie dsn ->")
  @Test
  void worksOk() {
    // GIVEN
    var MOVEaddr = BootstrapUtils.getPrimitiveAddress("MOVE");
    var nObjectsToMove = org.apache.commons.lang3.RandomUtils.nextInt(1, 20);
    var fromAddr = org.apache.commons.lang3.RandomUtils.insecure().randomInt(1000, 1500);
    var toAddr = org.apache.commons.lang3.RandomUtils.insecure().randomInt(1501, 2000);
    var cellsToMove =
        IntStream.range(0, nObjectsToMove)
            .boxed()
            .map(n -> Map.entry(fromAddr + n, new Object()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MOVEaddr)
            .withNextInstructionPointer(MOVEaddr + 1)
            .withMemory(aMemory().with(cellsToMove).build())
            .withParameterStack(aParameterStack().with(toAddr, fromAddr, nObjectsToMove).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var movedCells =
        cellsToMove.entrySet().stream()
            .map(e -> Map.entry(toAddr + (e.getKey() - fromAddr), e.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(referenceState).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(aMemory().with(referenceState).with(movedCells).build())
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("should throw if the top of the stack is not a number.")
  @Test
  void throwsIfTopNonNumber() {
    // GIVEN
    var MOVEaddr = BootstrapUtils.getPrimitiveAddress("MOVE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MOVEaddr)
            .withNextInstructionPointer(MOVEaddr + 1)
            .withParameterStack(aParameterStack().with(nextInt(), nextInt(), new Object()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @DisplayName("should throw if the 2nd top of the stack is not a number.")
  @Test
  void throwsIf2ndTopNonNumber() {
    // GIVEN
    var MOVEaddr = BootstrapUtils.getPrimitiveAddress("MOVE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MOVEaddr)
            .withNextInstructionPointer(MOVEaddr + 1)
            .withParameterStack(aParameterStack().with(nextInt(), new Object(), nextInt()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @DisplayName("should throw if the 3rd top of the stack is not a number.")
  @Test
  void throwsIf3rdTopNonNumber() {
    // GIVEN
    var MOVEaddr = BootstrapUtils.getPrimitiveAddress("MOVE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MOVEaddr)
            .withNextInstructionPointer(MOVEaddr + 1)
            .withParameterStack(aParameterStack().with(new Object(), nextInt(), nextInt()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var MOVEaddr = BootstrapUtils.getPrimitiveAddress("MOVE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MOVEaddr)
            .withNextInstructionPointer(MOVEaddr + 1)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }

  @DisplayName("should throw if ParameterStack has only 1 element.")
  @Test
  void throwIfOnlyOneElement() {
    // GIVEN
    var MOVEaddr = BootstrapUtils.getPrimitiveAddress("MOVE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MOVEaddr)
            .withNextInstructionPointer(MOVEaddr + 1)
            .withParameterStack(aParameterStack().with(nextInt()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @DisplayName("should throw if ParameterStack has only 2 elements.")
  @Test
  void throwIfOnly2Elements() {
    // GIVEN
    var MOVEaddr = BootstrapUtils.getPrimitiveAddress("MOVE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(MOVEaddr)
            .withNextInstructionPointer(MOVEaddr + 1)
            .withParameterStack(aParameterStack().with(nextInt(), nextInt()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }
}
