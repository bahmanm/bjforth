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

import bjforth.primitives.COMMALANGLE.MethodDescriptor;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RANGLECOMMATest {

  @DisplayName("Should push the result of the method call onto ParameterStack")
  @Test
  void worksOk() {
    // GIVEN
    var RANGLECOMMAaddr = getPrimitiveAddress(">,");
    Integer number = RandomUtils.insecure().randomInt();
    var methodDescriptor = new MethodDescriptor();
    methodDescriptor.parameterTypes = List.of(String.class);
    methodDescriptor.arity = 1;
    methodDescriptor.name = "valueOf";
    methodDescriptor.target = Integer.class;
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RANGLECOMMAaddr)
            .withParameterStack(
                aParameterStack().with(String.valueOf(number), methodDescriptor).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with(number).build());
  }

  @DisplayName("Should push the result of the method call onto ParameterStack")
  @Test
  void worksOkVarargs() {
    // GIVEN
    var RANGLECOMMAaddr = getPrimitiveAddress(">,");
    var methodDescriptor = new MethodDescriptor();
    methodDescriptor.parameterTypes = List.of(CharSequence.class, CharSequence[].class);
    methodDescriptor.arity = 3;
    methodDescriptor.name = "join";
    methodDescriptor.target = String.class;
    methodDescriptor.varargFromArgumentNo = 1;
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RANGLECOMMAaddr)
            .withParameterStack(
                aParameterStack()
                    .with((CharSequence) "world")
                    .with((CharSequence) "Hello")
                    .with((CharSequence) ",")
                    .with(methodDescriptor)
                    .build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with("Hello,world").build());
  }

  public static class Foo {
    public static void bar() {}
  }

  @DisplayName("Push null onto ParameterStack when method return type is void.")
  @Test
  void voidReturn() {
    // GIVEN
    var RANGLECOMMAaddr = getPrimitiveAddress(">,");
    var obj = new Foo();
    var methodDescriptor = new MethodDescriptor();
    methodDescriptor.target = Foo.class;
    methodDescriptor.parameterTypes = List.of();
    methodDescriptor.arity = 0;
    methodDescriptor.name = "bar";
    var actualState =
        aMachineState()
            .withInstrcutionPointer(RANGLECOMMAaddr)
            .withParameterStack(aParameterStack().with(methodDescriptor).build())
            .build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    assertThat(actualState).hasParameterStackEqualTo(aParameterStack().with((Object) null).build());
  }
}
