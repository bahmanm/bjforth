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
import bjforth.primitives.ATLANGLE.MethodDescriptor;
import java.lang.reflect.Array;
import org.apache.commons.lang3.reflect.ConstructorUtils;

public class RANGLEAT implements Primitive {
  @Override
  public void execute(Machine machine) {
    var methodDescriptor = (MethodDescriptor) machine.popFromParameterStack();

    var methodArity = methodDescriptor.arity;
    var arguments = new Object[methodArity];
    for (var i = 0; i < methodArity; i++) {
      var obj = machine.popFromParameterStack();
      arguments[i] = obj;
    }

    var paramTypes = new Class<?>[methodDescriptor.parameterTypes.size()];
    methodDescriptor.parameterTypes.toArray(paramTypes);
    var ctor =
        ConstructorUtils.getMatchingAccessibleConstructor(methodDescriptor.clazz, paramTypes);
    if (ctor == null) {
      throw new MachineException(
          "No such constructor found: %s/%d"
              .formatted(methodDescriptor.clazz, methodDescriptor.arity));
    }

    Object result;
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
              case 0 -> ctor.newInstance(varargs);
              case 1 -> ctor.newInstance(arguments[0], varargs);
              case 2 -> ctor.newInstance(arguments[0], arguments[1], varargs);
              case 3 -> ctor.newInstance(arguments[0], arguments[1], arguments[2], varargs);
              case 4 ->
                  ctor.newInstance(arguments[0], arguments[1], arguments[2], arguments[3], varargs);
              case 5 ->
                  ctor.newInstance(
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      varargs);
              case 6 ->
                  ctor.newInstance(
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      arguments[5],
                      varargs);
              case 7 ->
                  ctor.newInstance(
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      arguments[5],
                      arguments[6],
                      varargs);
              default ->
                  throw new MachineException(
                      "Failed to invoke the constructor: %s".formatted(ctor));
            };
      } else {
        result =
            switch (methodDescriptor.arity) {
              case 0 -> ctor.newInstance();
              case 1 -> ctor.newInstance(arguments[0]);
              case 2 -> ctor.newInstance(arguments[0], arguments[1]);
              case 3 -> ctor.newInstance(arguments[0], arguments[1], arguments[2]);
              case 4 -> ctor.newInstance(arguments[0], arguments[1], arguments[2], arguments[3]);
              case 5 ->
                  ctor.newInstance(
                      arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]);
              case 6 ->
                  ctor.newInstance(
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      arguments[5]);
              case 7 ->
                  ctor.newInstance(
                      arguments[0],
                      arguments[1],
                      arguments[2],
                      arguments[3],
                      arguments[4],
                      arguments[5],
                      arguments[6]);
              default ->
                  throw new MachineException(
                      "Failed to invoke the constructor: %s".formatted(ctor));
            };
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new MachineException(e.getMessage());
    }
    machine.pushToParameterStack(result);
  }

  @Override
  public String getName() {
    return ">@";
  }
}
