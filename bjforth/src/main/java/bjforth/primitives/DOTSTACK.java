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

import static bjforth.primitives.PrimitiveFactory.DROP;
import static bjforth.primitives.PrimitiveFactory.PRINTLN;

import bjforth.machine.Machine;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;
import java.util.Stack;

public class DOTSTACK implements Primitive {
  @Override
  public void execute(Machine machine) {
    var backupStack = new Stack<Object>();
    try {
      while (true) {
        var obj = machine.popFromParameterStack();
        backupStack.push(obj);
      }
    } catch (NoSuchElementException _ex) {
    }
    try {
      while (true) {
        var obj = backupStack.pop();
        machine.pushToParameterStack(obj);
        var str = "";
        if (obj == null) {
          str = "null";
        } else {
          str = "%s<%s>".formatted(obj.getClass().getTypeName(), obj.toString());
        }
        machine.pushToParameterStack(str);
        PRINTLN().execute(machine);
        DROP().execute(machine);
      }
    } catch (EmptyStackException _ex) {
    }
  }

  @Override
  public String getName() {
    return ".S";
  }
}
