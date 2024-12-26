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
package bjforth.machine;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StackTest {

  @Test
  void getPointerThrowIfEmpty() {
    // GIVEN
    var stack = new Stack();

    // EXPECT
    assertThrows(MachineException.class, stack::getPointer);
  }

  @Test
  void getPointerOk() {
    // GIVEN
    var stack = new Stack();
    stack.push(10);
    stack.push(20);

    // EXPECT
    assertThat(stack.getPointer()).isEqualTo(1);
  }

  @Test
  void setPointerThrowIfNegative() {
    // GIVEN
    var stack = new Stack();
    stack.push(10);

    // EXPECT
    assertThrows(
        MachineException.class,
        () -> {
          stack.setPointer(-1);
        });
  }

  @Test
  void setPointerThrowIfTooLarge() {
    // GIVEN
    var stack = new Stack();
    stack.push(10);
    stack.push(20);

    // EXPECT
    assertThrows(
        MachineException.class,
        () -> {
          stack.setPointer(2);
        });
  }

  @Test
  void setPointerOk() {
    // GIVEN
    var stack = new Stack();
    stack.push(10);
    stack.push(20);

    // GIVEN
    stack.setPointer(0);

    // THEN
    assertThat(stack.getPointer()).isEqualTo(0);
  }

  @Test
  void getItemThrowIfNegative() {
    // GIVEN
    var stack = new Stack();
    stack.push(10);

    // EXPECT
    assertThrows(
        MachineException.class,
        () -> {
          stack.getItem(-1);
        });
  }

  @Test
  void getItemThrowIfTooLarge() {
    // GIVEN
    var stack = new Stack();
    stack.push(10);
    stack.push(20);

    // EXPECT
    assertThrows(
        MachineException.class,
        () -> {
          stack.getItem(2);
        });
  }

  @Test
  void getItemOk() {
    // GIVEN
    var stack = new Stack();
    stack.push(10);
    stack.push(20);

    // GIVEN
    var item = stack.getItem(1);

    // THEN
    assertThat(item).isEqualTo(10);
  }
}
