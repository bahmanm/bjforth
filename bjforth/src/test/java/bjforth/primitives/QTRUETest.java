/*
 * Copyright 2025 Bahman Movaqar
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import bjforth.machine.MachineException;
import org.junit.jupiter.api.Test;

class QTRUETest {

  @Test
  void worksOkTrue() {
    // GIVEN
    var QTRUEaddr = getPrimitiveAddress("?TRUE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QTRUEaddr)
            .withParameterStack(aParameterStack().with(true).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(1).build());
  }

  @Test
  void worksOkFalse() {
    // GIVEN
    var QTRUEaddr = getPrimitiveAddress("?TRUE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QTRUEaddr)
            .withParameterStack(aParameterStack().with(false).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(0).build());
  }

  @Test
  void worksOkZero() {
    // GIVEN
    var QTRUEaddr = getPrimitiveAddress("?TRUE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QTRUEaddr)
            .withParameterStack(aParameterStack().with(0).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(0).build());
  }

  @Test
  void worksOkNonZero() {
    // GIVEN
    var QTRUEaddr = getPrimitiveAddress("?TRUE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QTRUEaddr)
            .withParameterStack(aParameterStack().with(100).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(1).build());
  }

  @Test
  void throwIfNotBooleanNotInteger() {
    // GIVEN
    var QTRUEaddr = getPrimitiveAddress("?TRUE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QTRUEaddr)
            .withParameterStack(aParameterStack().with(100.1).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
  }

  @Test
  void throwIfEmpty() {
    // GIVEN
    var QTRUEaddr = getPrimitiveAddress("?TRUE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QTRUEaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
  }
}
