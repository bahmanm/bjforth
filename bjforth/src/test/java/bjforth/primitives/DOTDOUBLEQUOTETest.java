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
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import bjforth.variables.Variables;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DOTDOUBLEQUOTETest {

  private final InputStream originalSystemIn = System.in;

  @AfterEach
  public void restoreSystemIn() {
    System.setIn(originalSystemIn);
  }

  @BeforeEach
  public void resetKEY() {
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
  }

  @Test
  void worksOkImmediate() {
    // GIVEN
    var DOTDOUBLEQUOTEaddr = getPrimitiveAddress(".\"");
    var str = " \tfoo \"bar\" \".";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var actualState = aMachineState().withInstrcutionPointer(DOTDOUBLEQUOTEaddr).build();
    var machine = aMachine().withState(actualState).build();

    var STATEaddr = Variables.get("STATE").getAddress();
    machine.setMemoryAt(STATEaddr, 0);

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(" \tfoo \"bar\"").build());
  }

  @Test
  void worksOkCompiling() {
    // GIVEN
    var DOTDOUBLEQUOTEaddr = getPrimitiveAddress(".\"");
    var str = " \tfoo \"bar\" \".";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var actualState = aMachineState().withInstrcutionPointer(DOTDOUBLEQUOTEaddr).build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    var STATEaddr = Variables.get("STATE").getAddress();
    machine.setMemoryAt(STATEaddr, 1);

    var HEREaddr = Variables.get("HERE").getAddress();
    var HEREvalue = (Integer) machine.getMemoryAt(HEREaddr);

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasMemoryEqualTo(
            aMemory()
                .with(referenceState)
                .with(STATEaddr, 1)
                .with(HEREaddr, HEREvalue + 2)
                .with(HEREvalue, getPrimitiveAddress("LIT"))
                .with(HEREvalue + 1, " \tfoo \"bar\"")
                .build());
  }

  @Test
  void emptyString() {
    // GIVEN
    var DOTDOUBLEQUOTEaddr = getPrimitiveAddress(".\"");
    var str = "\".";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var actualState = aMachineState().withInstrcutionPointer(DOTDOUBLEQUOTEaddr).build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with("").build());
  }
}
