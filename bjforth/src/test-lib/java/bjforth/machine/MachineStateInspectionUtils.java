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

import static org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField;

import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.function.FailableCallable;

public class MachineStateInspectionUtils {

  private static <R> R inspect(FailableCallable<R, IllegalAccessException> inspector) {
    try {
      return inspector.call();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static Set<Integer> memoryAddresses(MachineState ms) {
    return inspect(
        () -> {
          var cells = (Map<Integer, ?>) readDeclaredField(ms.getMemory(), "cells", true);
          return cells.keySet();
        });
  }

  @SuppressWarnings("unchecked")
  public static Set<String> dictionaryKeys(MachineState ms) {
    return inspect(
        () -> {
          var items = (Map<String, ?>) readDeclaredField(ms.getDictionary(), "items", true);
          return items.keySet();
        });
  }

  public static Integer returnStackSize(MachineState ms) {
    return inspect(
        () -> {
          var data = (Deque<?>) readDeclaredField(ms.getReturnStack(), "data", true);
          return data.size();
        });
  }

  @SuppressWarnings("unchecked")
  public static Iterator<Integer> returnStackDescendingIterator(MachineState ms) {
    return inspect(
        () -> {
          var data = (Deque<Integer>) readDeclaredField(ms.getReturnStack(), "data", true);
          return data.descendingIterator();
        });
  }

  public static Integer parameterStackSize(MachineState ms) {
    return inspect(
        () -> {
          var data = (Deque<?>) readDeclaredField(ms.getParameterStack(), "data", true);
          return data.size();
        });
  }

  @SuppressWarnings("unchecked")
  public static Iterator<Object> parameterStackDescendingIterator(MachineState ms) {
    return inspect(
        () -> {
          var data = (Deque<Object>) readDeclaredField(ms.getParameterStack(), "data", true);
          return data.descendingIterator();
        });
  }

  public static Integer instructionPointer(MachineState ms) {
    return ms.getInstructionPointer();
  }

  public static Integer nextInstructionPointer(MachineState ms) {
    return ms.getNextInstructionPointer();
  }
}
