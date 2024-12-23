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
import bjforth.machine.MachineException;
import bjforth.primitives.lib.ClassCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DOTLANGLE implements Primitive {

  private static enum State {
    BEGIN,
    IN_METHOD_NAME,
    IN_PARAM_TYPE,
    IN_MAYBE_VARARG,
    IN_VARARG,
    IN_ARITY,
    END
  }

  static class MethodDescriptor {

    String name = "";
    List<Class<?>> parameterTypes = new ArrayList<>();
    Boolean isVarargs = false;
    Integer arity = 0;
  }

  @Override
  public void execute(Machine machine) {
    var name = new StringBuilder();
    var state = State.BEGIN;
    var result = new MethodDescriptor();
    var parameterType = new StringBuilder();
    var arity = new StringBuilder();
    while (!state.equals(State.END)) {
      KEY().execute(machine);
      var s = (String) machine.popFromParameterStack();
      switch (state) {
        case State.BEGIN:
          if (!" ".equals(s) && !"\t".equals(s)) {
            state = State.IN_METHOD_NAME;
            name.append(s);
          }
          break;
        case State.IN_METHOD_NAME:
          if ("(".equals(s)) {
            state = State.IN_PARAM_TYPE;
          } else if (!" ".equals(s) && !"\t".equals(s)) {
            name.append(s);
            result.name = name.toString();
          }
          break;
        case State.IN_PARAM_TYPE:
          if ("(".equals(s)) {
            state = State.END;
          } else if (" ".equals(s) || "\t".equals(s) || "\n".equals(s)) {
            // Ignore whitespace
          } else if (",".equals(s)) {
            result.parameterTypes.add(ClassCache.forName(parameterType.toString()));
            parameterType = new StringBuilder();
          } else if (".".equals(s)) {
            state = State.IN_MAYBE_VARARG;
          } else {
            parameterType.append(s);
          }
          break;
        case State.IN_MAYBE_VARARG:
          if (".".equals(s)) {
            result.isVarargs = true;
            result.parameterTypes.add(ClassCache.forNameVararg(parameterType.toString()));
            state = State.IN_VARARG;
          } else {
            parameterType.append(".");
            parameterType.append(s);
            state = State.IN_PARAM_TYPE;
          }
          break;
        case State.IN_VARARG:
          if (")".equals(s)) {
            state = State.IN_ARITY;
          } else if (".".equals(s)) {
            // Ingore
          }
          break;
        case State.IN_ARITY:
          if ("/".equals(s)) {
            // Ignore
          } else if ("\t".equals(s) || " ".equals(s) || "\n".equals(s)) {
            result.arity = Integer.valueOf(arity.toString());
            state = State.END;
          } else {
            arity.append(s);
          }
        default:
          break;
      }
    }
    machine.pushToParameterStack(result);
  }

  @Override
  public String getName() {
    return ".<";
  }
}
