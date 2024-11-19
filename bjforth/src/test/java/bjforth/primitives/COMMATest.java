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

import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import bjforth.variables.Variables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class COMMATest {

  @Test
  @DisplayName("Set the memory and update HERE")
  public void worksOk() {
    // GIVEN
    var comma = PrimitiveFactory.COMMA();
    var commaAddr = nextInt();
    var ip = anInstructionPointer().with(commaAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var parameter = nextInt();
    var hereValue = nextInt();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(commaAddr, comma).build())
            .withParameterStack(aParameterStack().with(parameter).build())
            .withVariable(Variables.HERE(), hereValue)
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasMemoryEqualTo(
            aMemory()
                .with(state1)
                .with(hereValue, parameter)
                .with(Variables.HERE().getAddress(), hereValue + 1)
                .build())
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(state1);
  }

  @Test
  @DisplayName("Should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var comma = PrimitiveFactory.COMMA();
    var commaAddr = nextInt();
    var ip = anInstructionPointer().with(commaAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(commaAddr, comma).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }
}
