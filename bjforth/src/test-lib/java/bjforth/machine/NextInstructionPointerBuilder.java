/*
 * Copyright 2022 Bahman Movaqar
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

public class NextInstructionPointerBuilder {
  private Integer nextInstructionPointer;

  private NextInstructionPointerBuilder() {}

  public static NextInstructionPointerBuilder aNextInstructionPointer() {
    return new NextInstructionPointerBuilder();
  }

  public NextInstructionPointerBuilder with(MachineState ms) {
    nextInstructionPointer = ms.getNextInstructionPointer();
    return this;
  }

  public NextInstructionPointerBuilder withInstructionPointer(MachineState ms) {
    nextInstructionPointer = ms.getInstructionPointer();
    return this;
  }

  public NextInstructionPointerBuilder with(Integer nextInstructionPointer) {
    this.nextInstructionPointer = nextInstructionPointer;
    return this;
  }

  public NextInstructionPointerBuilder plus(Integer offset) {
    nextInstructionPointer += offset;
    return this;
  }

  public Integer build() {
    return nextInstructionPointer;
  }
}
