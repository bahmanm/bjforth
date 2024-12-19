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
      var ch = (int) machine.popFromParameterStack();
      switch (state) {
        case BEGIN:
          if (ch == '"') {
            state = State.MAYBE_END;
          } else {
            result.append((char) ch);
            state = State.IN_STRING;
          }
          break;
        case MAYBE_END:
          if (ch == '.') {
            state = State.END;
            result.deleteCharAt(result.length() - 1);
          } else {
            result.append("\"");
            result.append((char) ch);
            state = State.IN_STRING;
          }
          break;
        case IN_STRING:
          if (ch == '"') {
            state = State.MAYBE_END;
          } else {
            result.append((char) ch);
          }
          break;
      }
    }
    machine.pushToParameterStack(result.toString());
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
