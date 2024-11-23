/*
 * Copyright 2024 Bahman Movaqar
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

import static bjforth.machine.DictionaryBuilder.aDictionary;
import static bjforth.machine.DictionaryItemBuilder.aDictionaryItem;
import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import bjforth.machine.DictionaryItem;
import bjforth.machine.MachineException;
import bjforth.variables.Variables;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IMMEDIATETest {

  @Test
  @DisplayName("Toggles the value of Immediate flag for the word pointed to by LATEST.")
  public void worksOk() {
    // GIVEN
    var immediate = PrimitiveFactory.IMMEDIATE();
    var immediateAddr = nextInt();
    var ip = anInstructionPointer().with(immediateAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var wordName = RandomStringUtils.secure().next(5);
    var wordAddr = nextInt();
    var latestValue = nextInt();
    var isImmediate = nextBoolean();
    var dictItem = new DictionaryItem(wordName, wordAddr, isImmediate, false);
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(
                aMemory().with(immediateAddr, immediate).with(latestValue, wordAddr).build())
            .withDictionary(aDictionary().with(wordName, dictItem).build())
            .withParameterStack(aParameterStack().build())
            .withVariable(Variables.LATEST(), latestValue)
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(state2)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(state1).plus(1).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(state1).plus(1).build())
        .hasDictionaryEqualTo(
            aDictionary()
                .with(state1)
                .with(wordName, aDictionaryItem().with(dictItem).isImmediate(!isImmediate).build())
                .build())
        .hasMemoryEqualTo(
            aMemory().with(state1).with(Variables.LATEST().getAddress(), latestValue).build())
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(state1);
  }

  @Test
  @DisplayName("Throws if LATEST doesn't point to a dictionary item.")
  public void throwsIfMissingWord() {
    // GIVEN
    var immediate = PrimitiveFactory.IMMEDIATE();
    var immediateAddr = nextInt();
    var ip = anInstructionPointer().with(immediateAddr).build();
    var nip = aNextInstructionPointer().with(ip).plus(1).build();
    var latestValue = nextInt();
    var state1 =
        aMachineState()
            .withInstrcutionPointer(ip)
            .withNextInstructionPointer(nip)
            .withMemory(aMemory().with(immediateAddr, immediate).build())
            .withDictionary(aDictionary().build())
            .withParameterStack(aParameterStack().build())
            .withVariable(Variables.LATEST(), latestValue)
            .build();
    var state2 = aMachineState().copyFrom(state1).build();
    var machine = aMachine().withState(state2).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(state2).isEqualTo(state1);
  }
}
