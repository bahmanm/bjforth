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
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static org.junit.jupiter.api.Assertions.*;

import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EXITTest {

  @DisplayName("Restore NIP from ReturnStack")
  @Test
  void worksOk() {
    // GIVEN
    var EXITaddr = getPrimitiveAddress("EXIT");
    var NIPold = RandomUtils.insecure().randomInt(1000, 2000);
    var NIPnew = NIPold - 100;
    var actualState =
        aMachineState()
            .withReturnStack(aReturnStack().with(NIPnew).build())
            .withNextInstructionPointer(NIPold)
            .withInstrcutionPointer(EXITaddr)
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.enterThreadedCode();
    machine.step();

    // THEN
    assertThat(actualState)
        .hasMemoryEqualTo(referenceState)
        .hasReturnStackEqualTo(aReturnStack().build())
        .hasNextInstructionPointerEqualTo(NIPnew);
  }

  @Test
  @DisplayName("Should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var COMMAaddr = getPrimitiveAddress(",");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(COMMAaddr)
            .withNextInstructionPointer(COMMAaddr + 1)
            .withReturnStack(aReturnStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
