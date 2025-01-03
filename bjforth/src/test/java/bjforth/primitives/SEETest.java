/*
 * Copyright 2025 Bahman Movaqar
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
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SEETest {

  private final PrintStream originalSystemOut = System.out;
  private ByteArrayOutputStream outputStream = null;
  private PrintStream systemOut = null;
  private final InputStream originalSystemIn = System.in;

  @AfterEach
  public void restoreSystemIn() {
    System.setIn(originalSystemIn);
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
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
    outputStream = new ByteArrayOutputStream();
    systemOut = new PrintStream(outputStream);
    System.setOut(systemOut);
  }

  @Test
  void worksOkPrimitive() {
    // GIVEN
    var wordStr = "+";
    var str = wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var SEEaddr = getPrimitiveAddress("SEE");
    var actualState = aMachineState().withInstrcutionPointer(SEEaddr).build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(outputStream.toString()).containsIgnoringWhitespaces("Primitive", "ADD");
  }
}
