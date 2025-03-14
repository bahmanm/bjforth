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

import java.util.LinkedList;

/* the public interface is quite leaky and doesn't try to encapsulate the impl
   details at all - mostly b/c of the words related to stack pointers, ie RSP!,
   RSP@, DSP! and DSP@.
*/
class Stack {

  private final LinkedList<Object> data = new LinkedList<>();

  Stack() {}

  Stack(Stack other) {
    other.data.iterator().forEachRemaining(data::addLast);
  }

  public Object pop() {
    return data.removeFirst();
  }

  public void push(Object item) {
    data.addFirst(item);
  }

  public Object peek() {
    return data.peek();
  }

  public int getPointer() {
    if (data.size() <= 0) {
      throw new MachineException("Empty stack");
    }
    return data.size() - 1;
  }

  public void setPointer(int pointer) {
    if (pointer >= data.size() || pointer < 0) {
      throw new MachineException("Invalid stack pointer");
    }
    for (int i = data.size() - 1; i > pointer; i--) data.remove(i);
  }

  public Object getItem(int pointer) {
    if (pointer >= data.size() || pointer < 0) {
      throw new MachineException("Invalid stack pointer");
    }
    return data.get(pointer);
  }
}
