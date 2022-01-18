package bjforth.machine;

import java.util.Comparator;
import java.util.Deque;

import org.apache.commons.lang3.reflect.FieldUtils;

public class ParameterStackComparator implements Comparator<Stack<Object>> {

  private ParameterStackComparator() {}

  public static ParameterStackComparator aParameterStackComparator() {
    return new ParameterStackComparator();
  }

  @Override
  @SuppressWarnings("unchecked")
  public int compare(Stack<Object> s1, Stack<Object> s2) {
    try {
      var data1 = (Deque<Integer>) FieldUtils.readDeclaredField(s1, "data", true);
      var data2 = (Deque<Integer>) FieldUtils.readDeclaredField(s2, "data", true);
      if (data1.size() != data2.size())
        return 1;
      var i1 = data1.descendingIterator();
      var i2 = data2.descendingIterator();
      while (i1.hasNext()){
        if (! i1.next().equals(i2.next()))
          return 1;
      }
      return 0;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
