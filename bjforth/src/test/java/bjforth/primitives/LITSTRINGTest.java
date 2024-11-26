/*
 * Copyright 2022 Bahman Movaqar
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
import static bjforth.utils.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LITSTRINGTest {

  @DisplayName("pushes the address of string onto parameter stack and skips over the string.")
  @Test
  void worksOk() {
    // GIVEN
    var litstring = PrimitiveFactory.LITSTRING();
    var litstringAddr = nextInt();
    var ip = anInstructionPointer().with(litstringAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var string = RandomStringUtils.secure().next(10);
    var stringAddr = litstringAddr + 1;
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(litstringAddr, litstring).with(stringAddr, string).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(stringAddr).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(stringAddr).plus(2).build())
        .hasDictionaryEqualTo(state1)
        .hasMemoryEqualTo(state1)
        .hasParameterStackEqualTo(aParameterStack().with(stringAddr).build())
        .hasReturnStackEqualTo(state1);
  }

  @DisplayName("throws an exception in case NIP doesn't point to a string.")
  @Test
  void throwIfNoString() {
    // GIVEN
    var litstring = PrimitiveFactory.LITSTRING();
    var litstringAddr = nextInt();
    var ip = anInstructionPointer().with(litstringAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var invalidData = nextInt();
    var stringAddr = litstringAddr + 1;
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(
                aMemory().with(litstringAddr, litstring).with(stringAddr, invalidData).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(state2).isEqualTo(aMachineState().copyFrom(state1).build());
  }
}
