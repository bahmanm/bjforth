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
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TICKTest {

  @DisplayName("Push the address of the next word onto ParamterStack and skip it.")
  @Test
  void worksOk() {
    // GIVEN
    var ADDaddr = getPrimitiveAddress("+");
    var TICKaddr = getPrimitiveAddress("'");
    var IP = RandomUtils.insecure().randomInt(1000, 2000);
    var NIP = IP + 1;
    var actualState =
        aMachineState()
            .withInstrcutionPointer(IP)
            .withNextInstructionPointer(IP + 1)
            .withMemory(aMemory().with(IP, TICKaddr).with(NIP, ADDaddr).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step(2);

    // THEN
    assertThat(actualState)
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build())
        .hasParameterStackEqualTo(aParameterStack().with(ADDaddr).build());
  }
}
