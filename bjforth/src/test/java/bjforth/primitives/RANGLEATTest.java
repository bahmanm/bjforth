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

import static bjforth.machine.BootstrapUtils.getPrimitiveAddress;
import static bjforth.machine.MachineAssertions.assertThat;
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static bjforth.machine.ParameterStackBuilder.aParameterStack;

import bjforth.primitives.ATLANGLE.MethodDescriptor;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RANGLEATTest {

  @DisplayName("Should push the new object onto ParameterStack")
  @Test
  void worksOk() {
    // GIVEN
    var RANGLEAT = getPrimitiveAddress(">@");
    Integer number = RandomUtils.insecure().randomInt();
    var methodDescriptor = new MethodDescriptor();
    methodDescriptor.parameterTypes = List.of(String.class);
    methodDescriptor.arity = 1;
    methodDescriptor.varargFromArgumentNo = -1;
    methodDescriptor.clazz = Integer.class;
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RANGLEAT)
            .withParameterStack(
                aParameterStack().with(String.valueOf(number), methodDescriptor).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(number).build());
  }

  public static class VariadicCtor {
    String result = null;

    public VariadicCtor(String... args) {
      var result = new StringBuilder();
      Arrays.stream(args).forEach(result::append);
      this.result = result.toString();
    }

    @Override
    public boolean equals(Object other) {
      return result.equals(((VariadicCtor) other).result);
    }
  }

  @DisplayName("Should push the new object with variadic ctor call onto ParameterStack")
  @Test
  void worksOkVarargs() {
    // GIVEN
    var RANGLEAT = getPrimitiveAddress(">@");
    var methodDescriptor = new MethodDescriptor();
    methodDescriptor.parameterTypes = List.of(String[].class);
    methodDescriptor.arity = 3;
    methodDescriptor.clazz = VariadicCtor.class;
    methodDescriptor.varargFromArgumentNo = 0;
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RANGLEAT)
            .withParameterStack(
                aParameterStack()
                    .with("world")
                    .with(", ")
                    .with("Hello")
                    .with(methodDescriptor)
                    .build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState)
        .hasParameterStackEqualTo(aParameterStack().with(new VariadicCtor("Hello, world")).build());
  }
}
