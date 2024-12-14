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

import static bjforth.machine.BootstrapUtils.getPrimitiveAddress;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DSPSTORETest {

  @DisplayName("sets the parameter stack pointer to the value at the top of parameter stack")
  @Test
  void worksOk() {
    // GIVEN
    var DSPSTOREaddr = getPrimitiveAddress("DSP!");
    var pointerToStore = RandomUtils.nextInt(0, 5);
    var actualStateParameterStackElements =
        IntStream.range(0, pointerToStore + 5).mapToObj(i -> new Object()).toList();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPSTOREaddr)
            .withMemory(aMemory().build())
            .withParameterStack(
                aParameterStack()
                    .with(actualStateParameterStackElements)
                    .with(pointerToStore)
                    .build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackPointerEqualTo(pointerToStore)
        .hasParameterStackEqualTo(
            aParameterStack()
                .with(
                    actualStateParameterStackElements
                        .reversed()
                        .subList(0, pointerToStore + 1)
                        .reversed())
                .build());
  }

  @DisplayName("should throw if parameter stack is empty.")
  @Test
  void throwsIfParameterStackEmpty() {
    // GIVEN
    var DSPSTOREaddr = getPrimitiveAddress("DSP!");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPSTOREaddr)
            .withNextInstructionPointer(DSPSTOREaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(referenceState);
  }

  @DisplayName("should throw if parameter stack top is not a number.")
  @Test
  void throwsIfParameterStackNonNumber() {
    // GIVEN
    var DSPSTOREaddr = getPrimitiveAddress("DSP!");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPSTOREaddr)
            .withNextInstructionPointer(DSPSTOREaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(new Object()).build())
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

  @DisplayName("should throw if pointer is beyond parameter stack size.")
  @Test
  void throwsIfPointerTooLarge() {
    // GIVEN
    var DSPSTOREaddr = getPrimitiveAddress("DSP!");
    var pointer = RandomUtils.nextInt(5, 10);
    var parameterStackElements = List.of(nextInt(), nextInt());
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPSTOREaddr)
            .withNextInstructionPointer(DSPSTOREaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(
                aParameterStack().with(parameterStackElements).with(pointer).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().with(parameterStackElements).build())
                .build());
  }
}
