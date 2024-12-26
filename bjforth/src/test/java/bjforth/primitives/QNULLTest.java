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

import bjforth.machine.MachineException;
import org.junit.jupiter.api.Test;

class QNULLTest {

  @Test
  void worksOkNull() {
    // GIVEN
    var QNULLaddr = getPrimitiveAddress("?NULL");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QNULLaddr)
            .withParameterStack(aParameterStack().with((Object) null).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(1).build());
  }

  @Test
  void worksOkNonNull() {
    // GIVEN
    var QNULLaddr = getPrimitiveAddress("?NULL");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QNULLaddr)
            .withParameterStack(aParameterStack().with(new Object()).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(0).build());
  }

  @Test
  void throwIfEmpty() {
    // GIVEN
    var QNULLaddr = getPrimitiveAddress("?NULL");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QNULLaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
  }
}
