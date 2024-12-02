/*
 * Copyright 2023 Bahman Movaqar
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

import bjforth.primitives.PrimitiveFactory;
import bjforth.variables.Variables;

class Bootstrap {

  public void apply(MachineState state) {
    Variables.variables.forEach(
        (variable -> {
          state.getMemory().set(variable.getAddress(), variable.getInitialValue());
        }));
    for (int i = Variables.variables.size();
        i < PrimitiveFactory.getPrimitiveContainers().size() - 1;
        i++) {
      var primitive = PrimitiveFactory.getPrimitiveContainers().get(i).get();
      var dictItem =
          new DictionaryItem(primitive.getName(), i, primitive.isImmediate(), primitive.isHidden());
      state.getDictionary().put(primitive.getName(), dictItem);
      state.getMemory().set(i, primitive);
    }
    state
        .getMemory()
        .set(
            Variables.get("HERE").getAddress(),
            Variables.variables.size() + PrimitiveFactory.getPrimitiveContainers().size());

    state
        .getMemory()
        .set(
            Variables.get("LATEST").getAddress(),
            Variables.variables.size() + PrimitiveFactory.getPrimitiveContainers().size());
  }
}
