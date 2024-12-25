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
import bjforth.variables.Variables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class COMMALANGLE implements Primitive {

  private HashMap<String, Class<?>> clazzCache = new HashMap<>();

  private static enum State {
    BEGIN,
    IN_TYPE_NAME,
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
    Class<?> target = null;
    Integer arity = 0;
    Integer varargFromArgumentNo = -1;
  }

  @Override
  public void execute(Machine machine) {
    var target = new StringBuilder();
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
            state = State.IN_TYPE_NAME;
            target.append(s);
          }
          break;
        case State.IN_TYPE_NAME:
          if ("/".equals(s)) {
            var clazz = ClassCache.forName(target.toString());
            result.target = clazz;
            state = State.IN_METHOD_NAME;
          } else if (" ".equals(s) || "\t".equals(s)) {
            throw new MachineException("Invalid target class - contains whitespace.");
          } else {
            target.append(s);
          }
          break;
        case State.IN_METHOD_NAME:
          if ("(".equals(s)) {
            state = State.IN_PARAM_TYPE;
            result.name = name.toString();
          } else if (!" ".equals(s) && !"\t".equals(s)) {
            name.append(s);
          }
          break;
        case State.IN_PARAM_TYPE:
          if (" ".equals(s) || "\t".equals(s) || "\n".equals(s)) {
            // Ignore whitespace
          } else if (",".equals(s)) {
            result.parameterTypes.add(ClassCache.forName(parameterType.toString()));
            parameterType = new StringBuilder();
          } else if (".".equals(s)) {
            state = State.IN_MAYBE_VARARG;
          } else if (")".equals(s)) {
            result.parameterTypes.add(ClassCache.forName(parameterType.toString()));
            state = State.IN_ARITY;
          } else {
            parameterType.append(s);
          }
          break;
        case State.IN_MAYBE_VARARG:
          if (".".equals(s)) {
            result.parameterTypes.add(ClassCache.forNameVararg(parameterType.toString()));
            state = State.IN_VARARG;
          } else {
            parameterType.append(".");
            parameterType.append(s);
            state = State.IN_PARAM_TYPE;
          }
          break;
        case State.IN_VARARG:
          result.varargFromArgumentNo =
              result.parameterTypes.isEmpty() ? 0 : result.parameterTypes.size() - 1;
          if (")".equals(s)) {
            state = State.IN_ARITY;
          } else if (".".equals(s)) {
            // Ignore
          }
          break;
        case State.IN_ARITY:
          if ("/".equals(s)) {
            // Ignore
          } else if ("\t".equals(s) || " ".equals(s) || "\n".equals(s)) {
            try {
              result.arity = Integer.valueOf(arity.toString());
            } catch (NumberFormatException e) {
              throw new MachineException("Invalid method arity: '%s'".formatted(arity.toString()));
            }
            state = State.END;
          } else {
            arity.append(s);
          }
        default:
          break;
      }
    }

    var HEREaddr = Variables.get("HERE").getAddress();
    var HEREvalue = (Integer) machine.getMemoryAt(HEREaddr);
    machine.setMemoryAt(HEREvalue, machine.getDictionaryItem("LIT").get().getAddress());
    machine.setMemoryAt(HEREvalue + 1, result);
    machine.setMemoryAt(HEREaddr, (Integer) machine.getMemoryAt(HEREaddr) + 2);
  }

  @Override
  public String getName() {
    return ",<";
  }

  @Override
  public Boolean isImmediate() {
    return true;
  }
}
