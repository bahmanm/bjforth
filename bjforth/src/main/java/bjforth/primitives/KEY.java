/*
 * Copyright 2023 Bahman Movaqar
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

import bjforth.machine.GracefulShutdown;
import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.io.IOException;
import java.io.InputStreamReader;

class KEY implements Primitive {

  private InputStreamReader reader = null;

  private synchronized InputStreamReader getReader() {
    if (reader == null) {
      reader = new InputStreamReader(System.in);
    }
    return reader;
  }

  @Override
  public void execute(Machine machine) {
    try {
      var ch = getReader().read();
      if (ch == -1) {
        throw new GracefulShutdown();
      }
      machine.pushToParameterStack(String.valueOf((char) ch));
    } catch (IOException e) {
      throw new MachineException(e);
    }
  }
}
