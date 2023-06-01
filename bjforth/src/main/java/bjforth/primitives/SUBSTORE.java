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
package bjforth.primitives;

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import bjforth.primitives.lib.NumberUtils;
import java.util.NoSuchElementException;

public class SUBSTORE implements Primitive {

  @Override
  public void execute(Machine machine) {
    try {
      var addrObject = machine.popFromParameterStack();
      if (addrObject instanceof Integer address) {
        var numberObject = machine.getMemoryAt(address);
        var amountObject = machine.popFromParameterStack();
        if (numberObject instanceof Number number && amountObject instanceof Number amount) {
          var result = NumberUtils.sub(number, amount);
          machine.setMemoryAt(address, result);
        } else {
          throw new MachineException("Invalid arguments.");
        }
      } else {
        throw new MachineException("Invalid memory address");
      }
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }
}
