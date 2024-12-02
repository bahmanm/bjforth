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
package bjforth.machine;

import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;

import bjforth.variables.Variables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BootstrapTest {

  @Test
  @DisplayName("should place bjforth variable values in designated memory addresses")
  void placeVariables() {
    // GIVEN
    var actualState = aMachineState().build();
    var referenceState = aMachineState().copyFrom(actualState).build();
    var machine = aMachine().withState(referenceState).build();

    // EXPECT
    assertThat(referenceState)
        .hasMemoryEqualTo(
            aMemory()
                .with(referenceState)
                .with(Variables.get("HERE").getAddress(), 4)
                .with(Variables.get("STATE").getAddress(), 0)
                .with(Variables.get("BASE").getAddress(), 10)
                .with(Variables.get("LATEST").getAddress(), 0)
                .build());
  }

  @Test
  @DisplayName("should update HERE during & after bootstrap is done.")
  void updateHERE() {}
}
