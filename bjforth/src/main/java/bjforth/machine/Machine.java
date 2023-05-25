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

import java.util.Optional;

public class Machine {

  private MachineState state;

  public Machine(MachineState state) {
    this.state = state;
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

  public void pushToReturnStack(Object address) {
    state.getReturnStack().push(address);
  }

  public Object popFromReturnStack() {
    return state.getReturnStack().pop();
  }

  public Object getFromReturnStack(Integer index) {
    return state.getReturnStack().get(index);
  }

  public void setInReturnStack(Integer index, Object object) {
    state.getReturnStack().set(index, object);
  }

  public Optional<DictionaryItem> getDictionaryItem(String name) {
    return state.getDictionary().get(name);
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
    var ip = state.getInstructionPointer();
    var content = getMemoryAt(ip);
    if (content instanceof MachinePrimitive primitive) {
      primitive.execute(this);
    } else {
      throw new MachineException("don't know how to execute *(%d)".formatted(ip));
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
    for (var i = 0; i < n; i++) {
      step();
    }
  }

  /** Machine's "main loop". */
  public void loop() {
    while (true) {
      step();
    }
  }
}
