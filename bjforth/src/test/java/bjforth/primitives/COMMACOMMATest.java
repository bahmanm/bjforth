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

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

class COMMACOMMATest {

  @Test
  void worksOk() {
    // GIVEN
    var COMMACOMMAaddr = getPrimitiveAddress(",,");
    var number = RandomUtils.insecure().randomInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(COMMACOMMAaddr)
            .withParameterStack(
                aParameterStack().with(number, "valueOf/1", "java.lang.String").build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(String.valueOf(number)).build());
  }
}
