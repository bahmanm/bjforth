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
import static bjforth.machine.MachineAssertions.*;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.DictionaryItem;
import bjforth.machine.MachineException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FINDTest {

  @Test
  @DisplayName("Find a dictionary item.")
  public void worksOk() {
    // GIVEN
    var FINDaddr = getPrimitiveAddress("FIND");
    var wordToFind = "EMIT";
    var wordToFindAddr = getPrimitiveAddress("EMIT");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(FINDaddr)
            .withNextInstructionPointer(FINDaddr + 1)
            .withMemory(aMemory().build())
            .withParameterStack(aParameterStack().with(wordToFind).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(referenceState).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(referenceState)
        .hasParameterStackEqualTo(aParameterStack().with(wordToFindAddr).build())
        .hasReturnStackEqualTo(referenceState);
  }

  @Test
  @DisplayName("Should ignore hidden dictionary entries.")
  public void ignoreHidden() {
    // GIVEN
    var FINDaddr = getPrimitiveAddress("FIND");
    var wordToFind = "FOO";
    var wordToFindAddr = RandomUtils.insecure().randomInt(1000, 2000);
    var wordToFindIsHidden = true;
    var actualState =
        aMachineState()
            .withInstrcutionPointer(FINDaddr)
            .withNextInstructionPointer(FINDaddr + 1)
            .withMemory(aMemory().with(wordToFindAddr, new Object()).build())
            .withParameterStack(aParameterStack().with(wordToFind).build())
            .withDictionary(
                aDictionary()
                    .with(
                        wordToFind,
                        new DictionaryItem(wordToFind, wordToFindAddr, false, wordToFindIsHidden))
                    .build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(referenceState).plus(1).build())
        .hasNextInstructionPointerEqualTo(
            aNextInstructionPointer().with(referenceState).plus(1).build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(referenceState)
        .hasParameterStackEqualTo(aParameterStack().build())
        .hasReturnStackEqualTo(referenceState);
  }

  @Test
  @DisplayName("Should throw if ParameterStack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var FINDaddr = getPrimitiveAddress("FIND");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(FINDaddr)
            .withNextInstructionPointer(FINDaddr + 1)
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
