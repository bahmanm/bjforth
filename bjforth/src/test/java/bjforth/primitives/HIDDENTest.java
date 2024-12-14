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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bjforth.machine.DictionaryItem;
import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HIDDENTest {

  @Test
  @DisplayName("Toggles the value of Hidden flag for the word on the parameter stack.")
  public void worksOk() {
    // GIVEN
    var HIDDENaddr = getPrimitiveAddress("HIDDEN");
    var wordName = RandomStringUtils.secure().next(5);
    var wordAddr = nextInt();
    var latestValue = nextInt();
    var isHidden = nextBoolean();
    var dictItem = new DictionaryItem(wordName, wordAddr, false, isHidden);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(HIDDENaddr)
            .withDictionary(aDictionary().with(wordName, dictItem).build())
            .withMemory(aMemory().with(latestValue, wordAddr).build())
            .withParameterStack(aParameterStack().with(wordAddr).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();
    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasDictionaryEqualTo(
            aDictionary()
                .with(referenceState)
                .with(wordName, aDictionaryItem().with(dictItem).isHidden(!isHidden).build())
                .build());
  }

  @Test
  @DisplayName("Throws if the parameter doesn't point to a dictionary item.")
  public void throwsIfMissingWord() {
    // GIVEN
    var HIDDENaddr = getPrimitiveAddress("HIDDEN");
    var wordAddr = nextInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(HIDDENaddr)
            .withNextInstructionPointer(HIDDENaddr + 1)
            .withParameterStack(aParameterStack().with(wordAddr).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState)
        .isEqualTo(
            aMachineState()
                .copyFrom(referenceState)
                .withParameterStack(aParameterStack().build())
                .build());
  }

  @DisplayName("should throw if ParameterStack is already empty.")
  @Test
  void throwIfEmpty() {
    // GIVEN
    var HIDDENaddr = getPrimitiveAddress("HIDDEN");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(HIDDENaddr)
            .withNextInstructionPointer(HIDDENaddr + 1)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }
}
