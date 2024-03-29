/*
 * Copyright 2023 Bahman Movaqar
 *
 * This file is part of BJForth.
 *
 * BJForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BJForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BJForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.bootstrap;

import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static org.assertj.core.api.Assertions.*;

import bjforth.variables.Variables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BootstrapTest {

  @Test
  @DisplayName("should place bjforth variable values in designated memory addresses")
  void placeVariables() {
    // GIVEN
    var state1 = aMachineState().build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();
    var bootstrap = new Bootstrap();

    // WHEN
    bootstrap.apply(machine);

    // THEN
    assertThat(state2)
        .hasMemoryEqualTo(
            aMemory()
                .with(state1)
                .with(Variables.HERE().getAddress(), 4)
                .with(Variables.STATE().getAddress(), 0)
                .with(Variables.BASE().getAddress(), 10)
                .with(Variables.LATEST().getAddress(), 0)
                .build());
  }

  @Test
  @DisplayName("should update HERE during & after bootstrap is done.")
  void updateHERE() {}
}
