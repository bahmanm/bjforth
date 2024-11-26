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

public class InstructionPointerBuilder {
  private Integer instructionPointer;

  private InstructionPointerBuilder() {}

  public static InstructionPointerBuilder anInstructionPointer() {
    return new InstructionPointerBuilder();
  }

  public InstructionPointerBuilder with(MachineState ms) {
    instructionPointer = ms.getInstructionPointer();
    return this;
  }

  public InstructionPointerBuilder with(Integer instructionPointer) {
    this.instructionPointer = instructionPointer;
    return this;
  }

  public InstructionPointerBuilder plus(Integer offset) {
    instructionPointer += offset;
    return this;
  }

  public Integer build() {
    return instructionPointer;
  }
}
