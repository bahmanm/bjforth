/*
 * Copyright 2022 Bahman Movaqar
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
package bjforth.primitives;

import static bjforth.machine.MachineStatComparisonUtils.isEqualDictionaries;
import static bjforth.machine.MachineStatComparisonUtils.isEqualMemories;
import static bjforth.machine.MachineStatComparisonUtils.isEqualParameterStacks;
import static bjforth.machine.MachineStatComparisonUtils.isEqualReturnStack;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MachineStateInspectionUtils.*;
import static bjforth.machine.MemoryBuilder.aMemory;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bjforth.machine.Machine;


class DOCOLTest {

  @Test
  @DisplayName("it pushes FIP onto return stack and incs it by 1")
  public void worksOk() {
    // GIVEN
    var docol = new DOCOL();
    var docolAddress = nextInt();
    var state1 = aMachineState()
      .withMemory(aMemory()
                  .with(docolAddress, docol)
                  .build())
      .withInstrcutionPointer(docolAddress)
      .build();
    var state2 = aMachineState(state1);

    // WHEN
    new Machine(state2).step();

    // THEN
    assertThat(isEqualDictionaries(state1, state2)).isTrue();
    assertThat(isEqualMemories(state1, state2)).isTrue();
    assertThat(isEqualParameterStacks(state1, state2)).isTrue();
    assertThat(instructionPointer(state2)).isEqualTo(instructionPointer(state1) + 1);
    assertThat(forthInstructionPointer(state2)).isEqualTo(instructionPointer(state1) + 2);
    assertThat(isEqualReturnStack(state2, List.of(forthInstructionPointer(state1)))).isTrue();
  }
}
