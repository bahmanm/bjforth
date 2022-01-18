package bjforth.machine;

import static org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField;

import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MachineStateInspectionUtils {

  @SuppressWarnings("unchecked")
  public static Set<Integer> memoryAddresses(MachineState ms) {
    try {
      var cells = (Map<Integer,?>) readDeclaredField(ms.getMemory(), "cells", true);
      return cells.keySet();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static Set<String> dictionaryKeys(MachineState ms) {
    try {
      var items = (Map<String,?>) readDeclaredField(ms.getDictionary(), "items", true);
      return items.keySet();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static Integer returnStackSize(MachineState ms) {
    try {
      var data = (Deque<?>) readDeclaredField(ms.getReturnStack(), "data", true);
      return data.size();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static Iterator<Integer> returnStackDescendingIterator(MachineState ms) {
    try {
      var data = (Deque<Integer>) readDeclaredField(ms.getReturnStack(), "data", true);
      return data.descendingIterator();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static Integer parameterStackSize(MachineState ms) {
    try {
      var data = (Deque<?>) readDeclaredField(ms.getParameterStack(), "data", true);
      return data.size();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static Iterator<Object> parameterStackDescendingIterator(MachineState ms) {
    try {
      var data = (Deque<Object>) readDeclaredField(ms.getParameterStack(), "data", true);
      return data.descendingIterator();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static Integer instructionPointer(MachineState ms) {
    return ms.getInstructionPointer();
  }

  public static Integer forthInstructionPointer(MachineState ms) {
    return ms.getForthInstructionPointer();
  }
}
