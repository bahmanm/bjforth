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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

class ORTest {

  @Test
  void worksOk() {
    // GIVEN
    var ORaddr = getPrimitiveAddress("OR");
    var n1 = RandomUtils.insecure().randomInt(1000, 2000);
    var n2 = RandomUtils.insecure().randomInt(1000, 2000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ORaddr)
            .withParameterStack(aParameterStack().with(n1, n2).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(n1 | n2).build());
  }

  @Test
  void throwIfEmpty() {
    var ORaddr = getPrimitiveAddress("OR");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ORaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
  }

  @Test
  void throwIfOnlyOneElement() {
    var ORaddr = getPrimitiveAddress("OR");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(ORaddr)
            .withParameterStack(aParameterStack().with(10).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
  }
}
