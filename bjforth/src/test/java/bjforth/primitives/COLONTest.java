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
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static org.junit.jupiter.api.Assertions.*;

import bjforth.machine.DictionaryItem;
import bjforth.variables.Variables;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class COLONTest {

  private final InputStream originalSystemIn = System.in;

  @AfterEach
  public void restoreSystemIn() {
    System.setIn(originalSystemIn);
  }

  @BeforeEach
  public void resetPrimitives() {
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
  }

  @DisplayName("Calls CREATE, appends DOCOL, sets the word hidden and switch to compile mode")
  @Test
  void worksOk() {
    // GIVEN
    var wordStr = RandomStringUtils.insecure().next(10);
    var str = wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var COLONaddr = getPrimitiveAddress(":");
    var actualState = aMachineState().withInstrcutionPointer(COLONaddr).build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    var referenceLATESTvalue = (Integer) machine.getMemoryAt(Variables.get("LATEST").getAddress());
    var referenceHEREvalue = (Integer) machine.getMemoryAt(Variables.get("HERE").getAddress());

    // WHEN
    machine.step();

    // THEN
    var dictItem = new DictionaryItem(wordStr, referenceLATESTvalue + 1, false, true);
    assertThat(actualState)
        .hasDictionaryEqualTo(aDictionary().with(referenceState).with(wordStr, dictItem).build())
        .hasMemoryEqualTo(
            aMemory()
                .with(referenceState)
                .with(referenceLATESTvalue + 1, getPrimitiveAddress("DOCOL"))
                .with(Variables.get("HERE").getAddress(), referenceHEREvalue + 1)
                .with(Variables.get("STATE").getAddress(), 1)
                .with(Variables.get("LATEST").getAddress(), referenceLATESTvalue + 1)
                .build());
  }
}
