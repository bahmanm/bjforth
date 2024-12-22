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
package bjforth.primitives;

import static bjforth.primitives.PrimitiveFactory.KEY;

import bjforth.machine.Machine;

class WORD implements Primitive {

  private static enum State {
    BEGIN,
    END,
    IN_COMMENT,
    IN_WORD,
    IN_CHAR;
  }

  @Override
  public void execute(Machine machine) {
    var state = State.BEGIN;
    var result = new StringBuilder();
    while (state != State.END) {
      KEY().execute(machine);
      var s = (String) machine.popFromParameterStack();
      switch (state) {
        case BEGIN:
          if ("#".equals(s)) {
            state = State.IN_COMMENT;
          } else if (!" ".equals(s)
              && !"\t".equals(s)
              && !"\r".equals(s)
              && !"\b".equals(s)
              && !"\n".equals(s)) {
            result.append(s);
            state = State.IN_WORD;
          }
          break;
        case IN_COMMENT:
          if ("\n".equals(s)) {
            state = State.BEGIN;
          }
          break;
        case IN_WORD:
          if (" ".equals(s) || "\t".equals(s) || "\n".equals(s)) {
            state = State.END;
          } else {
            result.append(s);
          }
          break;
        default:
          break;
      }
    }
    machine.pushToParameterStack(result.toString());
  }
}
