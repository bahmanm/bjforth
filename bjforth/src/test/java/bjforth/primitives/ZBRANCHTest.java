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

import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.*;

import bjforth.machine.MachineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ZBRANCHTest {

  @Test
  @DisplayName("Move NIP by the offset stored at NIP in case of top of parameter stack is 0.")
  public void worksOkIfZero() {
    // GIVEN
    var zbranch = PrimitiveFactory.ZBRANCH();
    var zbranchAddr = nextInt();
    var ip = anInstructionPointer().with(zbranchAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var offset = nextInt();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(zbranchAddr, zbranch).with(zbranchAddr + 1, offset).build())
            .withParameterStack(aParameterStack().with(0).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(nip).plus(offset).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(state1).plus(offset).plus(1).build())
        .hasMemoryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(state1);
  }

  @Test
  @DisplayName("Skip over offset in case of top of parameter stack is 0.")
  public void worksOkIfNotZero() {
    // GIVEN
    var zbranch = PrimitiveFactory.ZBRANCH();
    var zbranchAddr = nextInt();
    var ip = anInstructionPointer().with(zbranchAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var offset = nextInt();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(zbranchAddr, zbranch).with(nip, offset).build())
            .withParameterStack(aParameterStack().with(1).build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(nip).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(2).build())
        .hasMemoryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(state1);
  }

  @Test
  @DisplayName("Should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var zbranch = PrimitiveFactory.ZBRANCH();
    var zbranchAddr = nextInt();
    var ip = anInstructionPointer().with(zbranchAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(zbranchAddr, zbranch).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }
}
