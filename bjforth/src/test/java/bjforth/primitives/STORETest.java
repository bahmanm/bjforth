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

class STORETest {

  @DisplayName("stores the value (2nd top of stack) at the given address (top of stack)")
  @Test
  void worksOk() {
    // GIVEN
    var STOREaddr = getPrimitiveAddress("!");
    var objectToStore = new Object();
    var addrToStore = RandomUtils.insecure().randomInt(1000, 2000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(STOREaddr)
            .withParameterStack(aParameterStack().with(objectToStore, addrToStore).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasMemoryEqualTo(aMemory().with(referenceState).with(addrToStore, objectToStore).build())
        .hasParameterStackEqualTo(aParameterStack().build());
  }

  @DisplayName("should throw if parameter stack top is not a number.")
  @Test
  void throwsIfNonNumber() {
    // GIVEN
    var STOREaddr = getPrimitiveAddress("!");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(STOREaddr)
            .withParameterStack(aParameterStack().with(new Object(), new Object()).build())
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
    var STOREaddr = getPrimitiveAddress("!");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(STOREaddr)
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
    var STOREaddr = getPrimitiveAddress("!");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(STOREaddr)
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
