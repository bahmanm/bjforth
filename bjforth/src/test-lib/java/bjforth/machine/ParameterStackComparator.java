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

import java.util.Comparator;
import java.util.Deque;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ParameterStackComparator implements Comparator<Stack> {

  private ParameterStackComparator() {}

  public static ParameterStackComparator aParameterStackComparator() {
    return new ParameterStackComparator();
  }

  @Override
  @SuppressWarnings("unchecked")
  public int compare(Stack s1, Stack s2) {
    try {
      var data1 = (Deque<Object>) FieldUtils.readDeclaredField(s1, "data", true);
      var data2 = (Deque<Object>) FieldUtils.readDeclaredField(s2, "data", true);
      if (data1.size() != data2.size()) return 1;
      var i1 = data1.descendingIterator();
      var i2 = data2.descendingIterator();
      while (i1.hasNext()) {
        if (!i1.next().equals(i2.next())) return 1;
      }
      return 0;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
