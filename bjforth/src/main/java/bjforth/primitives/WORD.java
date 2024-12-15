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

import bjforth.machine.Machine;
import bjforth.machine.MachineException;

class WORD implements Primitive {

  private static enum State {
    BEGIN,
    END,
    IN_COMMENT,
    IN_WORD;
  }

  @Override
  public void execute(Machine machine) {
    var state = State.BEGIN;
    var keyWord = PrimitiveFactory.KEY();
    var result = new StringBuilder();
    int parenthesesCount = 0;
    while (state != State.END) {
      keyWord.execute(machine);
      var ch = (int) machine.popFromParameterStack();
      switch (state) {
        case BEGIN:
          if (ch == '\\') {
            state = State.IN_COMMENT;
          }
          if (ch == '(') {
            state = State.IN_COMMENT;
            parenthesesCount += 1;
          } else if (ch != ' ' && ch != '\n') {
            result.appendCodePoint(ch);
            state = State.IN_WORD;
          }
          break;
        case IN_COMMENT:
          if (ch == '\n') {
            state = State.BEGIN;
          } else if (ch == ')') {
            if (parenthesesCount > 0) {
              parenthesesCount -= 1;
            } else {
              throw new MachineException("Imbalanced parentheses.");
            }
            if (parenthesesCount == 0) {
              state = State.BEGIN;
            }
          }
          break;
        case IN_WORD:
          if (ch == ' ' || ch == '\t' || ch == '\n') {
            state = State.END;
          } else {
            result.appendCodePoint(ch);
          }
          break;
        default:
          break;
      }
    }
    machine.pushToParameterStack(result.toString());
  }
}
