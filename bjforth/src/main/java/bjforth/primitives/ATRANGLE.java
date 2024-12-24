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
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.reflect.ConstructorUtils;

public class ATRANGLE implements Primitive {

  @Override
  public void execute(Machine machine) {
    var type = (String) machine.popFromParameterStack();
    var methodDescriptor = (String) machine.popFromParameterStack();

    var methodDescriptorSections = methodDescriptor.split("/");
    var methodName = methodDescriptorSections[0];
    if (!methodName.equals("new")) {
      throw new MachineException("Constructor method name must be 'new'.");
    }
    var methodArity = Integer.valueOf(methodDescriptorSections[1]);
    var arguments = new Object[methodArity];
    var argumentTypes = new Class[methodArity];
    for (var i = 0; i < methodArity; i++) {
      var obj = machine.popFromParameterStack();
      arguments[i] = obj;
      argumentTypes[i] = obj.getClass();
    }

    try {
      var clazz = Class.forName(type);
      Object result = null;
      result = ConstructorUtils.invokeConstructor(clazz, arguments, argumentTypes);
      machine.pushToParameterStack(result);
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException
        | ClassNotFoundException
        | InstantiationException e) {
      throw new MachineException(e.getMessage());
    }
  }

  @Override
  public String getName() {
    return "@>";
  }
}
