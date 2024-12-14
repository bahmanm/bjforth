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
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static bjforth.utils.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.MachineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TORTest {

  @DisplayName("pops the top of the parameter stack and pushes on to the return stack, ie a ->")
  @Test
  void worksOk() {
    // GIVEN
    var TORaddr = getPrimitiveAddress(">R");
    var object = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(TORaddr)
            .withParameterStack(aParameterStack().with(object).build())
            .withReturnStack(aReturnStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(aReturnStack().with(object).build());
  }

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var TORaddr = getPrimitiveAddress(">R");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(TORaddr)
            .withParameterStack(aParameterStack().build())
            .withReturnStack(aReturnStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
