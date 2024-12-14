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
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static org.junit.jupiter.api.Assertions.*;

import bjforth.machine.DictionaryItem;
import bjforth.variables.Variables;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SEMICOLONTest {

  @DisplayName(
      "Appends the EXIT codeword to the definition, toggles the hidden flag, switches to immediate mode")
  @Test
  void worksOk() {
    // GIVEN
    var SEMICOLONaddr = getPrimitiveAddress(";");
    var NIP = RandomUtils.insecure().randomInt(500, 1000);
    var LATESTvalue = RandomUtils.insecure().randomInt(1000, 2000);
    var HEREvalue = LATESTvalue + 10;
    var actualState =
        aMachineState()
            .withInstrcutionPointer(SEMICOLONaddr)
            .withReturnStack(aReturnStack().with(NIP).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();
    machine.createDictionaryItem("FOO", new DictionaryItem("FOO", LATESTvalue, false, true));
    machine.setMemoryAt(Variables.get("LATEST").getAddress(), LATESTvalue);
    machine.setMemoryAt(Variables.get("HERE").getAddress(), HEREvalue);

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasReturnStackEqualTo(aReturnStack().with(NIP).build());
    Assertions.assertThat(machine.getDictionaryItem("FOO").get().getIsHidden()).isFalse();
  }
}
