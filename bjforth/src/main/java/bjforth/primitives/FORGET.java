/*
 * Copyright 2024 Bahman Movaqar
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
package bjforth.primitives;

import static bjforth.primitives.PrimitiveFactory.FIND;
import static bjforth.primitives.PrimitiveFactory.WORD;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;

public class FORGET implements Primitive {
  @Override
  public void execute(Machine machine) {
    WORD().execute(machine);
    FIND().execute(machine);
    var addr = (Integer) machine.popFromParameterStack();
    var maybeDictItem = machine.getDictionaryItem(addr);
    if (maybeDictItem.isPresent()) {
      var dictItem = maybeDictItem.get();
      machine.removeDictionaryItem(dictItem.getName());
    } else {
      throw new MachineException("No such entry.");
    }
  }
}
