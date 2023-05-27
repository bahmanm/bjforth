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

import static bjforth.machine.MachineStateInspectionUtils.returnStackAscendingIterator;

import java.util.Arrays;
import java.util.List;

public class ReturnStackBuilder {
  private Stack returnStack = new Stack();

  private ReturnStackBuilder() {}

  public static ReturnStackBuilder aReturnStack() {
    return new ReturnStackBuilder();
  }

  public ReturnStackBuilder with(Object... objects) {
    return with(Arrays.asList(objects));
  }

  public ReturnStackBuilder with(List<Object> objects) {
    objects.forEach(returnStack::push);
    return this;
  }

  public ReturnStackBuilder with(MachineState ms) {
    returnStackAscendingIterator(ms).forEachRemaining(returnStack::push);
    return this;
  }

  public Stack build() {
    return returnStack;
  }
}
