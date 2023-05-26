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

import java.util.Arrays;
import java.util.List;

public class ParameterStackBuilder {
  private Stack parameterStack = new Stack();

  private ParameterStackBuilder() {}

  public static ParameterStackBuilder aParameterStack() {
    return new ParameterStackBuilder();
  }

  public ParameterStackBuilder with(Object... objects) {
    return with(Arrays.asList(objects));
  }

  public ParameterStackBuilder with(List<Object> objects) {
    objects.forEach(parameterStack::push);
    return this;
  }

  public ParameterStackBuilder with(MachineState otherState) {
    MachineStateInspectionUtils.parameterStackDescendingIterator(otherState)
        .forEachRemaining(parameterStack::push);
    return this;
  }

  public Stack build() {
    return parameterStack;
  }
}
