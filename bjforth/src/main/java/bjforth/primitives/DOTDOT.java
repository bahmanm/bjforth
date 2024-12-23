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
import org.apache.commons.lang3.reflect.MethodUtils;

public class DOTDOT implements Primitive {
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

    try {
      Object result = null;
      if (arguments.length != 0) {
        result = method.invoke(target, (Object) arguments);
      } else {
        result = method.invoke(target);
      }
      machine.pushToParameterStack(result);
    } catch (Exception e) {
      throw new MachineException(e.toString());
    }
  }

  @Override
  public String getName() {
    return "..";
  }
}
