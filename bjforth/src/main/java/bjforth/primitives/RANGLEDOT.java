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

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import bjforth.primitives.DOTLANGLE.MethodDescriptor;
import java.lang.reflect.Array;
import org.apache.commons.lang3.reflect.MethodUtils;

public class RANGLEDOT implements Primitive {
  @Override
  public void execute(Machine machine) {
    var methodDescriptor = (MethodDescriptor) machine.popFromParameterStack();
    var target = machine.popFromParameterStack();
    var methodArity = methodDescriptor.arity;
    var arguments = new Object[methodArity];
    for (var i = 0; i < methodArity; i++) {
      var obj = machine.popFromParameterStack();
      arguments[i] = obj;
    }

    var paramTypes = new Class<?>[methodDescriptor.parameterTypes.size()];
    methodDescriptor.parameterTypes.toArray(paramTypes);
    var method =
        MethodUtils.getAccessibleMethod(target.getClass(), methodDescriptor.name, paramTypes);
    if (method == null) {
      throw new MachineException(
          "No such method found: %s/%d".formatted(methodDescriptor.name, methodDescriptor.arity));
    }

    Object result = null;
    try {
      if (methodDescriptor.varargFromArgumentNo != -1) {
        var varargCount = methodDescriptor.arity - methodDescriptor.varargFromArgumentNo;
        var varargs =
            Array.newInstance(
                methodDescriptor
                    .parameterTypes
                    .get(methodDescriptor.varargFromArgumentNo)
                    .getComponentType(),
                varargCount);
        for (var i = 0; i < varargCount; i++) {
          Array.set(varargs, i, arguments[i + methodDescriptor.varargFromArgumentNo]);
        }
        result =
            switch (methodDescriptor.varargFromArgumentNo) {
              case 0 -> method.invoke(target, varargs);
              case 1 -> method.invoke(target, arguments[0], varargs);
              case 2 -> method.invoke(target, arguments[0], arguments[1], varargs);
              case 3 -> method.invoke(target, arguments[0], arguments[1], arguments[2], varargs);
              case 4 ->
                  method.invoke(
                      target, arguments[0], arguments[1], arguments[2], arguments[3], varargs);
              case 5 ->
                  method.invoke(
                      target,
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      varargs);
              case 6 ->
                  method.invoke(
                      target,
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      arguments[5],
                      varargs);
              case 7 ->
                  method.invoke(
                      target,
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      arguments[5],
                      arguments[6],
                      varargs);
              default -> null;
            };
      } else {
        result =
            switch (methodDescriptor.arity) {
              case 0 -> method.invoke(target);
              case 1 -> method.invoke(target, arguments[0]);
              case 2 -> method.invoke(target, arguments[0], arguments[1]);
              case 3 -> method.invoke(target, arguments[0], arguments[1], arguments[2]);
              case 4 ->
                  method.invoke(target, arguments[0], arguments[1], arguments[2], arguments[3]);
              case 5 ->
                  method.invoke(
                      target, arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]);
              case 6 ->
                  method.invoke(
                      target,
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      arguments[5]);
              case 7 ->
                  method.invoke(
                      target,
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      arguments[5],
                      arguments[6]);
              default -> null;
            };
      }
    } catch (Exception e) {
      throw new MachineException(e.getMessage());
    }
    machine.pushToParameterStack(result);
  }

  @Override
  public String getName() {
    return ">.";
  }
}
