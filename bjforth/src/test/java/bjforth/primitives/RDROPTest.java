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
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RDROPTest {

  @Test
  @DisplayName("drops the top of return stack.")
  public void workOk() {
    // GIVEN
    var RDROPaddr = getPrimitiveAddress("RDROP");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RDROPaddr)
            .withReturnStack(aReturnStack().with(nextInt()).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasReturnStackEqualTo(aReturnStack().build());
  }

  @Test
  @DisplayName("should throw if return stack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var RDROPaddr = getPrimitiveAddress("RDROP");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RDROPaddr)
            .withReturnStack(aReturnStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(referenceState);
  }
}
