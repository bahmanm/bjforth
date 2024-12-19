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

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.NoSuchElementException;

class EMIT implements Primitive {

  private OutputStreamWriter writer = null;

  private synchronized OutputStreamWriter getWriter() {
    if (writer == null) {
      writer = new OutputStreamWriter(System.out);
    }
    return writer;
  }

  @Override
  public void execute(Machine machine) {
    try {
      var obj = machine.popFromParameterStack();
      if (obj instanceof Integer ch) {
        getWriter().write(ch);
        getWriter().flush();
      } else if (obj instanceof String str) {
        getWriter().write(str);
        getWriter().flush();
      } else {
        throw new MachineException("Invalid codepoint");
      }
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    } catch (IOException e) {
      throw new MachineException(e);
    }
  }
}
