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

import bjforth.primitives.PrimitiveFactory;
import bjforth.variables.Variables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BootstrapTest {

  Integer totalVariablesAndWords =
      Variables.variables.size() + PrimitiveFactory.getPrimitiveContainers().size();

  @Test
  @DisplayName("should place bjforth variable values in designated memory addresses")
  void placeVariables() {
    // GIVEN
    var actualState = aMachineState().build();
    var machine = aMachine().withState(actualState).build();

    // EXPECT
    assertThat(actualState)
        .hasMemoryEqualTo(Variables.get("HERE").getAddress(), totalVariablesAndWords)
        .hasMemoryEqualTo(Variables.get("STATE").getAddress(), 0)
        .hasMemoryEqualTo(Variables.get("BASE").getAddress(), 10)
        .hasMemoryEqualTo(Variables.get("LATEST").getAddress(), totalVariablesAndWords - 1);
  }

  @Test
  @DisplayName("should update HERE")
  void updateHERE() {
    // GIVEN
    var actualState = aMachineState().build();
    var machine = aMachine().withState(actualState).build();

    // EXCEPT
    assertThat(actualState)
        .hasMemoryEqualTo(Variables.get("HERE").getAddress(), totalVariablesAndWords);
  }

  @Test
  @DisplayName("should update LATEST")
  void updateLATEST() {
    // GIVEN
    var actualState = aMachineState().build();
    var machine = aMachine().withState(actualState).build();

    // EXCEPT
    assertThat(actualState)
        .hasMemoryEqualTo(Variables.get("LATEST").getAddress(), totalVariablesAndWords - 1);
  }
}
