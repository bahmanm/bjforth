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

import java.util.Optional;
import java.util.stream.IntStream;

public class Machine {

  private MachineState state;

  public Machine(MachineState state) {
    this.state = state;
    new Bootstrap().apply(state);
  }

  public Object getMemoryAt(Integer address) {
    return state.getMemory().get(address);
  }

  public void setMemoryAt(Integer address, Object value) {
    state.getMemory().set(address, value);
  }

  public void pushToParameterStack(Object item) {
    state.getParameterStack().push(item);
  }

  public Object popFromParameterStack() {
    return state.getParameterStack().pop();
  }

  public int getParameterStackPointer() {
    return state.getParameterStack().getPointer();
  }

  public void setParameterStackPointer(int pointer) {
    state.getParameterStack().setPointer(pointer);
  }

  public void pushToReturnStack(Object address) {
    state.getReturnStack().push(address);
  }

  public Object popFromReturnStack() {
    return state.getReturnStack().pop();
  }

  public int getReturnStackPointer() {
    return state.getReturnStack().getPointer();
  }

  public void setReturnStackPointer(int pointer) {
    state.getReturnStack().setPointer(pointer);
  }

  public Optional<DictionaryItem> getDictionaryItem(String name) {
    return state.getDictionary().get(name);
  }

  public Optional<DictionaryItem> getDictionaryItem(Integer address) {
    return state.getDictionary().get(address);
  }

  public void createDictionaryItem(String name, DictionaryItem item) {
    state.getDictionary().put(name, item);
  }

  public Integer getInstrcutionPointer() {
    return state.getInstructionPointer();
  }

  public Integer getNextInstructionPointer() {
    return state.getNextInstructionPointer();
  }

  public void setNextInstructionPointer(Integer address) {
    state.setNextInstructionPointer(address);
  }

  public void jumpTo(Integer address) {
    state.setInstructionPointer(address);
  }

  /**
   * Executes exactly ONE memory cell and stops.
   *
   * <p>To be used for debugging/testing purposes.
   */
  public void step() {
    var IP = state.getInstructionPointer();
    var content = getMemoryAt(IP);
    if (content instanceof NativeSubroutine nativeSubroutine) {
      nativeSubroutine.call(this);
    } else {
      throw new MachineException("don't know how to execute *(%d)".formatted(IP));
    }
  }

  /**
   * Executes exactly N memory cells and stops.
   *
   * <p>To be used for debugging/testing purposes.
   *
   * @param n N memory cells
   */
  public void step(int n) {
    IntStream.range(0, n).forEach((_i) -> step());
  }

  /** Machine's "main loop". */
  public void loop() {
    while (true) {
      step();
    }
  }
}
