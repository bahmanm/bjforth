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
import bjforth.variables.Variables;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

class STOREHERETest {

  @Test
  void worksOk() {
    // GIVEN
    var STOREHEREaddr = getPrimitiveAddress("!HERE");
    var newValue = RandomUtils.insecure().randomInt(1000, 2000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(STOREHEREaddr)
            .withParameterStack(aParameterStack().with(newValue).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasVariableEqualTo(Variables.get("HERE"), newValue)
        .hasParameterStackEqualTo(aParameterStack().build());
  }

  @Test
  public void throwIfEmpty() {
    // GIVEN
    var STOREHEREaddr = getPrimitiveAddress("!HERE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(STOREHEREaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
