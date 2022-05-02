/*
 * Copyright 2022 Bahman Movaqar
 *
 * This file is part of BJForth.
 *
 * BJForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BJForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BJForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.machine;

public class MachineStateBuilder {
  private Integer instructionPointer = 0;
  private Integer nextInstructionPointer = 0;
  private Memory memory = new Memory();
  private Dictionary dictionary = new Dictionary();
  private Stack<Integer> returnStack = new Stack<Integer>();
  private Stack<Object> parameterStack = new Stack<Object>();

  private MachineStateBuilder() {}

  public static MachineStateBuilder aMachineState() {
    return new MachineStateBuilder();
  }

  public static MachineState aMachineState(MachineState other) {
    return new MachineState(other);
  }

  public MachineStateBuilder withInstrcutionPointer(Integer instructionPointer) {
    this.instructionPointer = instructionPointer;
    return this;
  }

  public MachineStateBuilder withNextInstructionPointer(Integer nextInstructionPointer) {
    this.nextInstructionPointer = nextInstructionPointer;
    return this;
  }

  public MachineStateBuilder withMemory(Memory memory) {
    this.memory = memory;
    return this;
  }

  public MachineStateBuilder withDictionary(Dictionary dictionary) {
    this.dictionary = dictionary;
    return this;
  }

  public MachineStateBuilder withReturnStack(Stack<Integer> returnStack) {
    this.returnStack = returnStack;
    return this;
  }

  public MachineStateBuilder withParameterStack(Stack<Object> parameterStack) {
    this.parameterStack = parameterStack;
    return this;
  }

  public MachineStateBuilder copyFrom(MachineState other) {
    var copy = new MachineState(other);
    dictionary = copy.getDictionary();
    instructionPointer = copy.getInstructionPointer();
    nextInstructionPointer = copy.getNextInstructionPointer();
    memory = copy.getMemory();
    parameterStack = copy.getParameterStack();
    returnStack = copy.getReturnStack();
    return this;
  }

  public MachineState build() {
    return new MachineState(
        instructionPointer,
        nextInstructionPointer,
        memory,
        dictionary,
        returnStack,
        parameterStack);
  }
}
