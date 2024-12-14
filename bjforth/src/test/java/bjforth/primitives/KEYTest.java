/*
 * Copyright 2023 Bahman Movaqar
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
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bjforth.machine.MachineException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KEYTest {

  private final InputStream originalSystemIn = System.in;

  @AfterEach
  public void restoreSystemIn() {
    System.setIn(originalSystemIn);
  }

  @BeforeEach
  public void resetKEY() {
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
  }

  @DisplayName(
      "reads a single character from System.in and pushes the value on to parameter stack.")
  @Test
  void worksOk() {
    // GIVEN
    var str = RandomStringUtils.random(1);
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var KEYaddr = getPrimitiveAddress("KEY");
    var actualState = aMachineState().withInstrcutionPointer(KEYaddr).build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(str.codePointAt(0)).build());
  }

  @DisplayName("throws if reaches end of input stream.")
  @Test
  void throwsIfEndOfStream() {
    // GIVEN
    var str = "";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var KEYaddr = getPrimitiveAddress("KEY");
    var actualState = aMachineState().withInstrcutionPointer(KEYaddr).build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step)
        .isInstanceOf(MachineException.class)
        .hasMessage("End of stream");
    assertThat(actualState).isEqualTo(referenceState);
  }
}
