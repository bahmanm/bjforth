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

import bjforth.machine.DictionaryItem;
import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import bjforth.variables.Variables;
import java.util.NoSuchElementException;

public class CREATE implements Primitive {

  @Override
  public void execute(Machine machine) {
    try {
      var nameObj = machine.popFromParameterStack();
      var HEREAddr = Variables.get("HERE").getAddress();
      var HEREValue = (Integer) machine.getMemoryAt(HEREAddr);
      var LATESTAddr = Variables.get("LATEST").getAddress();

      var name = "";
      if (nameObj instanceof String nameStr) {
        name = nameStr;
      } else if (nameObj instanceof Character nameCh) {
        name = nameCh.toString();
      } else {
        throw new MachineException("Invalid argument");
      }
      machine.setMemoryAt(LATESTAddr, HEREValue);
      var dictItem = new DictionaryItem(name, HEREValue, false, false);
      machine.createDictionaryItem(name, dictItem);
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }
}
