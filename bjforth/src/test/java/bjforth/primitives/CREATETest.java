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

import static bjforth.machine.BootstrapUtils.getPrimitiveAddress;
import static bjforth.machine.DictionaryBuilder.aDictionary;
import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.DictionaryItem;
import bjforth.machine.MachineException;
import bjforth.variables.Variables;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CREATETest {

  @Test
  @DisplayName("Create the dictionary item and the header.")
  public void worksOk() {
    // GIVEN
    var CREATEaddr = getPrimitiveAddress("CREATE");
    var nameAddr = nextInt();
    var name = RandomStringUtils.insecure().next(10);
    var nameLength = name.length();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(CREATEaddr)
            .withNextInstructionPointer(CREATEaddr + 1)
            .withMemory(aMemory().with(nameAddr, name).build())
            .withParameterStack(aParameterStack().with(nameAddr, nameLength).build())
            .withVariable(Variables.get("LATEST"), 0)
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();
    var HEREaddr = Variables.get("HERE").getAddress();
    var HEREvalue = (Integer) machine.getMemoryAt(HEREaddr);

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(referenceState).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build())
        .hasDictionaryEqualTo(
            aDictionary()
                .with(referenceState)
                .with(name, new DictionaryItem(name, HEREvalue, false, true))
                .build())
        .hasMemoryEqualTo(
            aMemory()
                .with(actualState)
                .with(HEREaddr, HEREvalue + 1)
                .with(Variables.get("LATEST").getAddress(), HEREvalue)
                .with(HEREvalue, name)
                .build())
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(actualState);
  }

  @Test
  @DisplayName("Should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var CREATEaddr = getPrimitiveAddress("CREATE");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(CREATEaddr)
            .withNextInstructionPointer(CREATEaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
