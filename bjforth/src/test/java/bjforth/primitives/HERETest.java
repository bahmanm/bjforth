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
import static org.junit.jupiter.api.Assertions.*;

import bjforth.variables.Variables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HERETest {
  @Test
  @DisplayName("Push the value of HERE to parameter stack.")
  public void worksOk() {
    // GIVEN
    var HEREaddr = getPrimitiveAddress("HERE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(HEREaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(
            aParameterStack()
                .with(
                    Variables.variables.size()
                        + PrimitiveFactory.getPrimitiveContainers()
                            .size()) /* TODO This assertion is likely to fail as soon as
                                     bootstrapping extends to non-primitive words. */
                .build());
  }
}
