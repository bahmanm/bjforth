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
import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.NextInstructionPointerBuilder.aNextInstructionPointer;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static bjforth.machine.ReturnStackBuilder.aReturnStack;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bjforth.machine.MachineException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QUITTest {

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
  @DisplayName("drops the top of return stack.")
  public void workOk() {
    // GIVEN
    var wordStr = "+";
    var str = wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var QUITaddr = getPrimitiveAddress("QUIT");
    var INTERPRETaddr = getPrimitiveAddress("INTERPRET");
    var offset = RandomUtils.insecure().randomInt(501, 1000);
    var nip = RandomUtils.insecure().randomInt(1000, 1500);
    var actualState =
        aMachineState()
            .withInstrcutionPointer(QUITaddr)
            .withNextInstructionPointer(nip)
            .withParameterStack(aParameterStack().with(1, 2).build())
            .withReturnStack(aReturnStack().with(RandomUtils.insecure().randomInt()).build())
            .withMemory(aMemory().with(nip, offset).build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(anInstructionPointer().with(INTERPRETaddr).build())
        .hasNextInstructionPointerEqualTo(aNextInstructionPointer().with(nip).build())
        .hasDictionaryEqualTo(referenceState)
        .hasMemoryEqualTo(referenceState)
        .hasReturnStackEqualTo(aReturnStack().build());
  }

  @Test
  @DisplayName("should throw if return stack is already empty.")
  public void throwIfEmpty() {
    // GIVEN
    var RDROPaddr = getPrimitiveAddress("RDROP");
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RDROPaddr)
            .withNextInstructionPointer(RDROPaddr + 1)
            .withReturnStack(aReturnStack().build())
            .build();
    var machine = aMachine().withState(actualState).build();
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThrows(MachineException.class, machine::step);
    assertThat(actualState).isEqualTo(referenceState);
  }
}
