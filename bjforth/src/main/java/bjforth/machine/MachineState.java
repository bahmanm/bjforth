/*
 * Copyright 2022 Bahman Movaqar
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
package bjforth.machine;

class MachineState {
  private Integer instructionPointer;
  private Integer nextInstructionPointer;
  private Memory memory;
  private Dictionary dictionary;
  private Stack returnStack;
  private Stack parameterStack;

  MachineState(
      Integer instructionPointer,
      Integer nextInstructionPointer,
      Memory memory,
      Dictionary dictionary,
      Stack returnStack,
      Stack parameterStack) {
    this.instructionPointer = instructionPointer;
    this.nextInstructionPointer = nextInstructionPointer;
    this.memory = memory;
    this.dictionary = dictionary;
    this.returnStack = returnStack;
    this.parameterStack = parameterStack;
  }

  MachineState(MachineState other) {
    instructionPointer = other.instructionPointer;
    nextInstructionPointer = other.nextInstructionPointer;
    memory = new Memory(other.memory);
    dictionary = new Dictionary(other.dictionary);
    returnStack = new Stack(other.returnStack);
    parameterStack = new Stack(other.parameterStack);
  }

  void setInstructionPointer(Integer instructionPointer) {
    this.instructionPointer = instructionPointer;
  }

  void setNextInstructionPointer(Integer nextInstructionPointer) {
    this.nextInstructionPointer = nextInstructionPointer;
  }

  Integer getInstructionPointer() {
    return instructionPointer;
  }

  Integer getNextInstructionPointer() {
    return nextInstructionPointer;
  }

  Memory getMemory() {
    return memory;
  }

  Dictionary getDictionary() {
    return dictionary;
  }

  Stack getReturnStack() {
    return returnStack;
  }

  Stack getParameterStack() {
    return parameterStack;
  }
}
