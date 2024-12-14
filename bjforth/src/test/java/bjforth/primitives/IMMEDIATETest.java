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
import static bjforth.machine.DictionaryBuilder.aDictionary;
import static bjforth.machine.DictionaryItemBuilder.aDictionaryItem;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    var IMMEDIATEaddr = getPrimitiveAddress("IMMEDIATE");
    var wordName = RandomStringUtils.secure().next(5);
    var wordAddr = nextInt();
    var latestValue = nextInt();
    var isImmediate = nextBoolean();
    var dictItem = new DictionaryItem(wordName, wordAddr, isImmediate, false);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(IMMEDIATEaddr)
            .withMemory(aMemory().with(latestValue, wordAddr).build())
            .withDictionary(aDictionary().with(wordName, dictItem).build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("LATEST").getAddress(), latestValue);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasDictionaryEqualTo(
            aDictionary()
                .with(referenceState)
                .with(wordName, aDictionaryItem().with(dictItem).isImmediate(!isImmediate).build())
                .build())
        .hasMemoryEqualTo(
            aMemory()
                .with(referenceState)
                .with(Variables.get("LATEST").getAddress(), latestValue)
                .build());
  }

  @Test
  @DisplayName("Throws if LATEST doesn't point to a dictionary item.")
  public void throwsIfMissingWord() {
    // GIVEN
    var IMMEDIATEaddr = getPrimitiveAddress("IMMEDIATE");
    var immediate = PrimitiveFactory.IMMEDIATE();
    var immediateAddr = nextInt();
    var latestValue = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(IMMEDIATEaddr)
            .withNextInstructionPointer(IMMEDIATEaddr + 1)
            .withMemory(aMemory().with(immediateAddr, immediate).build())
            .withDictionary(aDictionary().build())
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("LATEST").getAddress(), latestValue);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(referenceState);
  }

  @DisplayName("IMMEDIATE is an immediate word.")
  @Test
  void isImmediate() {
    // GIVEN
    var actualState = aMachineState().build();
    var machine = aMachine().withState(actualState).build();

    // EXCEPT
    org.assertj.core.api.Assertions.assertThat(
            machine.getDictionaryItem("IMMEDIATE").get().getIsImmediate())
        .isTrue();
  }
}
