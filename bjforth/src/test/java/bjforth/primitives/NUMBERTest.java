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
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NUMBERTest {

  @Test
  @DisplayName("Convert top of ParameterStack to number.")
  public void worksOk() {
    // GIVEN
    var NUMBERaddr = getPrimitiveAddress("NUMBER");
    var parameter = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(NUMBERaddr)
            .withParameterStack(aParameterStack().with(Integer.toString(parameter)).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(parameter).with(0).build());
  }

  @Test
  @DisplayName("Attempt to convert an invalid parameter to a number.")
  public void invalidParameter() {
    // GIVEN
    var NUMBERaddr = getPrimitiveAddress("NUMBER");
    var parameter = RandomStringUtils.secure().next(4);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(NUMBERaddr)
            .withParameterStack(aParameterStack().with(parameter).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with((Object) null).with(-1).build());
  }

  @Test
  @DisplayName("Should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var NUMBERaddr = getPrimitiveAddress("NUMBER");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(NUMBERaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
