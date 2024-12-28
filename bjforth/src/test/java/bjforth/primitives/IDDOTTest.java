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

import static bjforth.config.Constants.BACKGROUND_COLOR;
import static bjforth.config.Constants.FOREGROUND_COLOR;
import static bjforth.machine.BootstrapUtils.getPrimitiveAddress;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static com.diogonunes.jcolor.Ansi.colorize;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import bjforth.machine.MachineException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IDDOTTest {

  private final PrintStream originalSystemOut = System.out;
  private ByteArrayOutputStream outputStream = null;
  private PrintStream systemOut = null;

  @AfterEach
  public void restoreSystemIn() {
    System.setOut(originalSystemOut);
    systemOut.close();
    systemOut = null;
    try {
      outputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      outputStream = null;
    }
  }

  @BeforeEach
  public void setupSystemOut() {
    outputStream = new ByteArrayOutputStream();
    systemOut = new PrintStream(outputStream);
    System.setOut(systemOut);
  }

  @Test
  void worksOk() {
    // GIVEN
    var IDDOTaddr = getPrimitiveAddress("ID.");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(IDDOTaddr)
            .withParameterStack(aParameterStack().with(getPrimitiveAddress("CREATE")).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var expectedStr = new StringBuilder();
    expectedStr.append(
        colorize(
            "%s isImmediate: %s, isHidden: %s".formatted("CREATE", false, false),
            FOREGROUND_COLOR,
            BACKGROUND_COLOR));
    assertThat(outputStream.toString()).isEqualTo(expectedStr.toString());
  }

  @Test
  void throwIfEmpty() {
    // GIVEN
    var IDDOTaddr = getPrimitiveAddress("ID.");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(IDDOTaddr)
            .withParameterStack(aParameterStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
  }

  @Test
  void throwIfNotFound() {
    // GIVEN
    var IDDOTaddr = getPrimitiveAddress("ID.");
    var address = RandomUtils.insecure().randomInt(5000, 10000);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(IDDOTaddr)
            .withParameterStack(aParameterStack().with(address).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
  }
}
