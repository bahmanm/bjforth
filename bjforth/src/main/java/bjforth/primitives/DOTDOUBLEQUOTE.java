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

import static bjforth.primitives.PrimitiveFactory.KEY;

import bjforth.machine.Machine;
import bjforth.variables.Variables;

public class DOTDOUBLEQUOTE implements Primitive {

  private static enum State {
    BEGIN,
    IN_STRING,
    MAYBE_END,
    END;
  }

  @Override
  public void execute(Machine machine) {
    var result = new StringBuffer();
    Boolean isEnd = false;
    State state = State.BEGIN;
    while (!state.equals(State.END)) {
      KEY().execute(machine);
      var s = (String) machine.popFromParameterStack();
      switch (state) {
        case BEGIN:
          if ("\"".equals(s)) {
            state = State.MAYBE_END;
          } else {
            result.append(s);
            state = State.IN_STRING;
          }
          break;
        case MAYBE_END:
          if (".".equals(s)) {
            state = State.END;
            if (!result.isEmpty()) {
              result.deleteCharAt(result.length() - 1);
            }
          } else {
            result.append("\"");
            result.append(s);
            state = State.IN_STRING;
          }
          break;
        case IN_STRING:
          if ("\"".equals(s)) {
            state = State.MAYBE_END;
          } else {
            result.append(s);
          }
          break;
      }
    }
    var STATEaddr = Variables.get("STATE").getAddress();
    var STATEvalue = (Integer) machine.getMemoryAt(STATEaddr);
    if (STATEvalue == 1) {
      var HEREaddr = Variables.get("HERE").getAddress();
      var HEREvalue = (Integer) machine.getMemoryAt(HEREaddr);
      machine.setMemoryAt(HEREvalue, machine.getDictionaryItem("LIT").get().getAddress());
      machine.setMemoryAt(HEREvalue + 1, result.toString());
      machine.setMemoryAt(HEREaddr, (Integer) machine.getMemoryAt(HEREaddr) + 2);
    } else {
      machine.pushToParameterStack(result.toString());
    }
  }

  @Override
  public String getName() {
    return ".\"";
  }

  @Override
  public Boolean isImmediate() {
    return true;
  }
}
