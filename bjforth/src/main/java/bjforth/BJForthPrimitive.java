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

/**
 * Represents the words which either cannot be impl'ed in Forth or are
 * better done in Java because of, eg, performance.
 */
public interface BJForthPrimitive {
  /**
   * What the word is supposed to do?
   *
   * @param state current state which is most definitely going to be
   *              mutated by this primitive
   */
  void execute(BJForthState state);

  /**
   * A convenience method to wrap around the primitive logic to make sure
   * <tt>instructionPointer</tt> progresses forward. This is similar to
   * the <tt>NEXT</tt> macro as per <i>jonesforth</i>.
   *
   * @param state current state which is going to be passed along to
   *              {@link #execute(BJForthState) execute}.
   */
  default void doExecute(BJForthState state) {
    execute(state);
    state.instructionPointer(state.instructionPointer() + 1);
  }
}
