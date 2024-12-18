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
import static bjforth.machine.InstructionPointerBuilder.anInstructionPointer;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.MemoryBuilder.aMemory;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;
import static org.assertj.core.api.Assertions.*;

import bjforth.machine.MachineException;
import bjforth.variables.Variables;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class INTERPRETTest {

  private final InputStream originalSystemIn = System.in;

  @AfterEach
  public void restoreSystemIn() {
    System.setIn(originalSystemIn);
  }

  @BeforeEach
  public void resetPrimitives() {
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
  }

  @DisplayName("Immediate mode: Executes the word.")
  @Test
  void immediateExecuteWord() {
    // GIVEN
    var wordStr = "+";
    var str = wordStr + " ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var INTERPRETaddr = getPrimitiveAddress("INTERPRET");
    var actualState = aMachineState().withInstrcutionPointer(INTERPRETaddr).build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("STATE").getAddress(), 0);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasInstructionPointerEqualTo(
            anInstructionPointer().with(machine.getDictionaryItem("+").get().getAddress()).build());
  }

  @DisplayName("Compiling mode: replaces the word with its CFA at HERE.")
  @Test
  void compilingReplaceWordWithAddress() {
    // GIVEN
    var wordStr = "+";
    var str = wordStr + " ";
    var wordAddr = getPrimitiveAddress("+");
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var INTERPRETaddr = getPrimitiveAddress("INTERPRET");
    var actualState = aMachineState().withInstrcutionPointer(INTERPRETaddr).build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("STATE").getAddress(), 1);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var HEREdereferenced = (Integer) machine.getMemoryAt(Variables.get("HERE").getAddress());
    assertThat(actualState)
        .hasVariableEqualTo(Variables.get("HERE"), HEREdereferenced)
        .hasMemoryEqualTo(
            aMemory()
                .with(referenceState)
                .with(Variables.get("HERE").getAddress(), HEREdereferenced)
                .with(HEREdereferenced - 1, wordAddr)
                .build())
        .hasDictionaryEqualTo(referenceState)
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("Compiling mode: Replace a literal number with LIT <number>.")
  @Test
  void compilingReplaceNumberWithLITAndNumber() {
    // GIVEN
    var number = RandomUtils.insecure().randomInt();
    var str = "%d ".formatted(number);
    var LITaddr = getPrimitiveAddress("LIT");
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var INTERPRETaddr = getPrimitiveAddress("INTERPRET");
    var actualState = aMachineState().withInstrcutionPointer(INTERPRETaddr).build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("STATE").getAddress(), 1);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var HEREdereferenced = (Integer) machine.getMemoryAt(Variables.get("HERE").getAddress());
    assertThat(actualState)
        .hasVariableEqualTo(Variables.get("HERE"), HEREdereferenced)
        .hasMemoryEqualTo(
            aMemory()
                .with(referenceState)
                .with(Variables.get("HERE").getAddress(), HEREdereferenced)
                .with(HEREdereferenced - 2, LITaddr)
                .with(HEREdereferenced - 1, number)
                .build())
        .hasDictionaryEqualTo(referenceState)
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("Immediate mode: Push the literal number onto ParameterStack.")
  @Test
  void immediatePushNumberToStack() {
    // GIVEN
    var number = RandomUtils.insecure().randomInt();
    var str = "%d ".formatted(number);
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var INTERPRETaddr = getPrimitiveAddress("INTERPRET");
    var actualState = aMachineState().withInstrcutionPointer(INTERPRETaddr).build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("STATE").getAddress(), 0);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var HEREdereferenced = (Integer) machine.getMemoryAt(Variables.get("HERE").getAddress());
    assertThat(actualState)
        .hasVariableEqualTo(Variables.get("HERE"), HEREdereferenced)
        .hasParameterStackEqualTo(aParameterStack().with(number).build());
  }

  @DisplayName("Invalid input - not a word, not a number, not a character")
  @Test
  void invalidInput() {
    // GIVEN
    var str = "%s ".formatted(RandomStringUtils.insecure().next(15));
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var INTERPRETaddr = getPrimitiveAddress("INTERPRET");
    var actualState = aMachineState().withInstrcutionPointer(INTERPRETaddr).build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("STATE").getAddress(), 0);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // EXPECT
    assertThatThrownBy(machine::step).isInstanceOf(MachineException.class);
    assertThat(actualState).isEqualTo(aMachineState().copyFrom(referenceState).build());
  }

  @DisplayName("Compiling mode: Replace a literal object with LIT <object as string>.")
  @Test
  void compilingReplaceStringLiteralWithLITAndString() {
    // GIVEN
    var number = RandomUtils.insecure().randomInt();
    var randomStr = RandomStringUtils.insecure().next(10);
    var str = "%s ".formatted(randomStr);
    var LITaddr = getPrimitiveAddress("LIT");
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var INTERPRETaddr = getPrimitiveAddress("INTERPRET");
    var actualState = aMachineState().withInstrcutionPointer(INTERPRETaddr).build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("STATE").getAddress(), 1);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var HEREdereferenced = (Integer) machine.getMemoryAt(Variables.get("HERE").getAddress());
    assertThat(actualState)
        .hasVariableEqualTo(Variables.get("HERE"), HEREdereferenced)
        .hasMemoryEqualTo(
            aMemory()
                .with(referenceState)
                .with(Variables.get("HERE").getAddress(), HEREdereferenced)
                .with(HEREdereferenced - 2, LITaddr)
                .with(HEREdereferenced - 1, randomStr)
                .build())
        .hasDictionaryEqualTo(referenceState)
        .hasReturnStackEqualTo(referenceState);
  }

  @DisplayName("Immediate mode: Push the literal string onto ParameterStack.")
  @Test
  void immediatePushCharacterToStack() {
    // GIVEN
    var number = RandomUtils.insecure().randomInt();
    var randomStr = RandomStringUtils.insecure().next(10);
    var str = "%s\n".formatted(randomStr);
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var INTERPRETaddr = getPrimitiveAddress("INTERPRET");
    var actualState = aMachineState().withInstrcutionPointer(INTERPRETaddr).build();
    var machine = aMachine().withState(actualState).build();
    machine.setMemoryAt(Variables.get("STATE").getAddress(), 0);
    var referenceState = aMachineState().copyFrom(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var HEREdereferenced = (Integer) machine.getMemoryAt(Variables.get("HERE").getAddress());
    assertThat(actualState)
        .hasVariableEqualTo(Variables.get("HERE"), HEREdereferenced)
        .hasParameterStackEqualTo(aParameterStack().with(randomStr).build());
  }
}
