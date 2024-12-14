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

import static bjforth.machine.BootstrapUtils.getPrimitiveAddress;
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class COPYTest {

  @DisplayName(
      "copies the value pointed to by the top of the stack to the address pointed to by the 2nd top of the stack, ie ab ->")
  @Test
  void worksOk() {
    // GIVEN
    var COPYaddr = getPrimitiveAddress("COPY");
    var objectToCopy = new Object();
    var fromAddr = RandomUtils.insecure().randomInt(1000, 2000);
    var toAddr = RandomUtils.insecure().randomInt(2001, 3000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(COPYaddr)
            .withNextInstructionPointer(COPYaddr + 1)
            .withMemory(aMemory().with(fromAddr, objectToCopy).build())
            .withParameterStack(aParameterStack().with(toAddr, fromAddr).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasMemoryEqualTo(aMemory().with(referenceState).with(toAddr, objectToCopy).build())
        .hasParameterStackEqualTo(aParameterStack().build());
  }

  @DisplayName("should throw if the top of the stack is not a number.")
  @Test
  void throwsIfTopNonNumber() {
    // GIVEN
    var COPYaddr = getPrimitiveAddress("COPY");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(COPYaddr)
            .withNextInstructionPointer(COPYaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(nextInt(), new Object()).build())
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
    var COPYaddr = getPrimitiveAddress("COPY");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(COPYaddr)
            .withNextInstructionPointer(COPYaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(new Object(), nextInt()).build())
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
    var COPYaddr = getPrimitiveAddress("COPY");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(COPYaddr)
            .withNextInstructionPointer(COPYaddr + 1)
            .withMemory(aMemory().build())
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
    var COPYaddr = getPrimitiveAddress("COPY");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(COPYaddr)
            .withNextInstructionPointer(COPYaddr + 1)
            .withMemory(aMemory().build())
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
}
