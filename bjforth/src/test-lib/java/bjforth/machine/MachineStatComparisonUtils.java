package bjforth.machine;

import static bjforth.machine.MachineStateInspectionUtils.dictionaryKeys;
import static bjforth.machine.MachineStateInspectionUtils.memoryAddresses;
import static bjforth.machine.MachineStateInspectionUtils.parameterStackDescendingIterator;
import static bjforth.machine.MachineStateInspectionUtils.returnStackDescendingIterator;

import java.util.List;

public class MachineStatComparisonUtils {

  public static boolean isEqualMachineStates(MachineState ms1, MachineState ms2) {
    return isEqualDictionaries(ms1, ms2)
      && isEqualForthInstructionPointers(ms1, ms2)
      && isEqualInstructionPointers(ms1, ms2)
      && isEqualMemories(ms1, ms2)
      && isEqualReturnStacks(ms1, ms2)
      && isEqualParameterStacks(ms1, ms2);
  }

  public static boolean isEqualMemories(MachineState ms1, MachineState ms2) {
    var addresses1 = memoryAddresses(ms1);
    var addresses2 = memoryAddresses(ms2);
    if (addresses1.size() != addresses2.size())
      return false;
    return addresses1.stream().allMatch(a -> {
        var v1 = ms1.getMemory().get(a);
        var v2 = ms2.getMemory().get(a);
        return v1.equals(v2);
      });
  }

  public static boolean isEqualDictionaries(MachineState ms1, MachineState ms2) {
    var keys1 = dictionaryKeys(ms1);
    var keys2 = dictionaryKeys(ms2);
    if (keys1.size() != keys2.size())
      return false;
    return keys1.stream().allMatch(k -> {
      var v1 = ms1.getDictionary().get(k);
      var v2 = ms2.getDictionary().get(k);
      return v1.equals(v2);
      });
  }

  public static boolean isEqualReturnStacks(MachineState ms1, MachineState ms2) {
    var i1 = returnStackDescendingIterator(ms1);
    var i2 = returnStackDescendingIterator(ms2);
    while (i1.hasNext())
      if (! i2.hasNext() || ! i1.next().equals(i2.next()))
        return false;
    return ! i2.hasNext();
  }

  public static boolean isEqualParameterStacks(MachineState ms1, MachineState ms2) {
    var i1 = parameterStackDescendingIterator(ms1);
    var i2 = parameterStackDescendingIterator(ms2);
    while (i1.hasNext())
      if (! i2.hasNext() || ! i1.next().equals(i2.next()))
        return false;
    return ! i2.hasNext();
  }

  public static boolean isEqualInstructionPointers(MachineState ms1, MachineState ms2) {
    return ms1.getInstructionPointer().equals(ms2.getInstructionPointer());
  }

  public static boolean isEqualForthInstructionPointers(MachineState ms1, MachineState ms2) {
    return ms1.getForthInstructionPointer().equals(ms2.getForthInstructionPointer());
  }

  public static boolean isEqualReturnStack(MachineState ms1, List<Integer> list) {
    var i1 = returnStackDescendingIterator(ms1);
    var i2 = list.iterator();
    while (i1.hasNext())
      if (! i2.hasNext() || ! i1.next().equals(i2.next()))
        return false;
    return ! i2.hasNext();
  }
}
