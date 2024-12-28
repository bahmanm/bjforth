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
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import bjforth.machine.MachineException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FORGETTest {

  private final InputStream originalSystemIn = System.in;

  @AfterEach
  public void restoreSystemIn() {
    System.setIn(originalSystemIn);
  }

  @BeforeEach
  public void resetPrimitives() {
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
  }

  @Test
  void worksOk() {
    // GIVEN
    var wordStr = "+";
    var str = wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var FORGETaddr = getPrimitiveAddress("FORGET");
    var actualState = aMachineState().withInstrcutionPointer(FORGETaddr).build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(machine.getDictionaryItem("+")).isEmpty();
  }

  @Test
  void throwIfNotExists() {
    // GIVEN
    var wordStr = "A-WORD-THAT-DOES-NOT-EXIST!";
    var str = wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var FORGETaddr = getPrimitiveAddress("FORGET");
    var actualState = aMachineState().withInstrcutionPointer(FORGETaddr).build();
    var machine = aMachine().withState(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
  }
}
