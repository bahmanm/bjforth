/*
 * Copyright 2022 Bahman Movaqar
 *
 * This file is part of BJForth.
 *
 * BJForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BJForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BJForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.machine;

public class ForthInstructionPointerBuilder {
  private Integer forthInstructionPointer;

  private ForthInstructionPointerBuilder() {
  }

  public static ForthInstructionPointerBuilder aForthInstructionPointer() {
    return new ForthInstructionPointerBuilder();
  }

  public ForthInstructionPointerBuilder with(MachineState ms) {
    forthInstructionPointer = ms.getForthInstructionPointer();
    return this;
  }

  public ForthInstructionPointerBuilder withInstructionPointer(MachineState ms) {
    forthInstructionPointer = ms.getInstructionPointer();
    return this;
  }

  public ForthInstructionPointerBuilder with(Integer forthInstructionPointer) {
    this.forthInstructionPointer = forthInstructionPointer;
    return this;
  }

  public ForthInstructionPointerBuilder plus(Integer offset) {
    forthInstructionPointer += offset;
    return this;
  }

  public Integer build() {
    return forthInstructionPointer;
  }
}
