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

import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.assertThat;

import bjforth.machine.BootstrapUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PRINTLNTest {

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
    var PRINTLNaddr = BootstrapUtils.getPrimitiveAddress("PRINTLN");
    var n = RandomUtils.insecure().randomInt();
    var actualState =
        aMachineState()
            .withInstrcutionPointer(anInstructionPointer().with(PRINTLNaddr).build())
            .withParameterStack(aParameterStack().with(n).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(referenceState).build());
    assertThat(outputStream.toByteArray()).containsExactly("%d\n".formatted(n).getBytes());
  }
}
