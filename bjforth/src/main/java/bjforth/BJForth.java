/*
 * Copyright 2022 Bahman Movaqar.
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
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth;

public class BJForth {

  private final BJForthState state = new BJForthState();

  public void mainLoop() {
    while (true) {
      var ip = state.instructionPointer();
      var content = state.memory().get(ip);
      if (content instanceof BJForthPrimitive primitive) {
        primitive.doExecute(state);
      } else if (content instanceof Integer address) {
        state.instructionPointer(address);
      } else {
        throw new BJForthException("unknown memory content");
      }
    }
  }

  public static void main(String... args) {
    System.out.println("Hello, world");
  }
}
