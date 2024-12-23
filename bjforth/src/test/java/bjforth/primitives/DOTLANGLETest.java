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
import static bjforth.machine.MachineBuilder.aMachine;
import static bjforth.machine.MachineStateBuilder.aMachineState;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bjforth.primitives.DOTLANGLE.MethodDescriptor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DOTLANGLETest {

  private final InputStream originalSystemIn = System.in;

  @AfterEach
  public void restoreSystemIn() {
    System.setIn(originalSystemIn);
  }

  @BeforeEach
  public void resetKEY() {
    PrimitiveFactoryModificationUtils.resetAllPrimitives();
  }

  @Test
  void worksOkFullyQualified() {
    // GIVEN
    var str = "format(java.lang.String, java.lang.Object...)/2 ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var DOTLANGLEaddr = getPrimitiveAddress(".<");
    var actualState = aMachineState().withInstrcutionPointer(DOTLANGLEaddr).build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var actualResult = (MethodDescriptor) machine.popFromParameterStack();
    assertThat(actualResult.name).isEqualTo("format");
    assertThat(actualResult.isVarargs).isTrue();
    assertThat(actualResult.parameterTypes).isEqualTo(List.of(String.class, Object[].class));
    assertThat(actualResult.arity).isEqualTo(2);
  }

  @Test
  void worksOkUnqualified() {
    // GIVEN
    var str = "format(List, File, String...)/12 ";
    var inputStream = new ByteArrayInputStream(str.getBytes());
    System.setIn(inputStream);

    var DOTLANGLEaddr = getPrimitiveAddress(".<");
    var actualState = aMachineState().withInstrcutionPointer(DOTLANGLEaddr).build();
    var machine = aMachine().withState(actualState).build();

    // WHEN
    machine.step();

    // THEN
    var actualResult = (MethodDescriptor) machine.popFromParameterStack();
    assertThat(actualResult.name).isEqualTo("format");
    assertThat(actualResult.isVarargs).isTrue();
    assertThat(actualResult.parameterTypes)
        .isEqualTo(List.of(List.class, File.class, String[].class));
    assertThat(actualResult.arity).isEqualTo(12);
  }
}
