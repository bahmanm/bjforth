/*
 * Copyright 2024 Bahman Movaqar
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
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DSPFETCHFETCHTest {

  @Test
  void worksOk() {
    // GIVEN
    var DSPFETCHFETCHaddr = getPrimitiveAddress("DSP@@");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPFETCHFETCHaddr)
            .withParameterStack(aParameterStack().with(10, 20, 30, 40, 50, 3).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(10, 20, 30, 40, 50, 20).build());
  }

  @Test
  @DisplayName("Should throw if not a number")
  public void throwIfNotNumber() {
    // GIVEN
    var DSPFETCHFETCHaddr = getPrimitiveAddress("DSP@@");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPFETCHFETCHaddr)
            .withParameterStack(aParameterStack().with(10, 20, new Object()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(10, 20).build());
  }

  @Test
  @DisplayName("Should throw if null")
  public void throwIfNull() {
    // GIVEN
    var DSPFETCHFETCHaddr = getPrimitiveAddress("DSP@@");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(DSPFETCHFETCHaddr)
            .withParameterStack(aParameterStack().with(10, 20, null).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(10, 20).build());
  }
}
