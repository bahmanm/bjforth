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

import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.BootstrapUtils;
import bjforth.machine.MachineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ROTTest {

  @Test
  @DisplayName("rotates the top 3 elements of stack, ie abc -> bca.")
  void worksOk() {
    // GIVEN
    var ROTaddr = BootstrapUtils.getPrimitiveAddress("ROT");
    var parameter1 = nextInt();
    var parameter2 = nextInt();
    var parameter3 = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ROTaddr)
            .withParameterStack(aParameterStack().with(parameter3, parameter2, parameter1).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(
            aParameterStack().with(parameter2, parameter1, parameter3).build());
  }

  @Test
  @DisplayName("should throw if ParameterStack is already empty.")
  void throwIfEmpty() {
    // GIVEN
    var ROTaddr = BootstrapUtils.getPrimitiveAddress("ROT");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ROTaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }

  @Test
  @DisplayName("should throw if ParameterStack has only 1 element.")
  void throwIfParameterStackOneElement() {
    // GIVEN
    var ROTaddr = BootstrapUtils.getPrimitiveAddress("ROT");
    var parameter = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ROTaddr)
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @Test
  @DisplayName("should throw if ParameterStack has only 2 elements.")
  void throwIfParameterStackTwoElements() {
    // GIVEN
    var ROTaddr = BootstrapUtils.getPrimitiveAddress("ROT");
    var parameter1 = nextInt();
    var parameter2 = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ROTaddr)
            .withParameterStack(aParameterStack().with(parameter2, parameter1).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }
}
