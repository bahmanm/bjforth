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

import static bjforth.machine.ForthInstructionPointerBuilder.aForthInstructionPointer;
import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MachineStateInspectionUtils.*;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

import bjforth.machine.Machine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DOCOLTest {

  @Test
  @DisplayName("it pushes FIP onto return stack and incs it by 1")
  public void worksOk() {
    // GIVEN
    var docol = new DOCOL();
    var docolAddress = nextInt();
    var state1 = aMachineState().withMemory(aMemory().with(docolAddress, docol).build())
        .withInstrcutionPointer(docolAddress).build();
    var state2 = aMachineState(state1);

    // WHEN
    new Machine(state2).step();

    // THEN
    assertThat(state2).hasDictionaryEqualTo(state1).hasMemoryEqualTo(state1)
        .hasParameterStackEqualTo(state1)
        .hasReturnStackEqualTo(
            aReturnStack().with(state1).with(forthInstructionPointer(state1)).build())
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasForthInstructionPointerEqualTo(
            aForthInstructionPointer().withInstructionPointer(state1).plus(2).build());
  }
}
