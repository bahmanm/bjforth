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

import bjforth.machine.Machine;
import bjforth.machine.MachineException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;

public class TELL implements Primitive {
  @Override
  public void execute(Machine machine) {
    try {
      var streamObj = machine.popFromParameterStack();
      var addressObj = machine.popFromParameterStack();
      var _length = machine.popFromParameterStack();
      if (streamObj instanceof OutputStream stream && addressObj instanceof Integer address) {
        var stringObj = machine.getMemoryAt(address);
        if (stringObj instanceof String string) {
          stream.write(string.getBytes());
        } else {
          throw new MachineException("Invalid type");
        }
      } else {
        throw new MachineException("Invalid type");
      }
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error");
    } catch (IOException e) {
      throw new MachineException("Output stream failure.");
    }
  }
}
