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

import static bjforth.primitives.PrimitiveFactory.*;

import bjforth.machine.Machine;
import bjforth.variables.Variables;

public class SEMICOLON implements Primitive {
  @Override
  public void execute(Machine machine) {
    machine.DOCOL(true);
    machine.enterThreadedCode(); // TODO TO be removed.
    var HEREvalue = (Integer) machine.getMemoryAt(Variables.get("HERE").getAddress());
    machine.setMemoryAt(HEREvalue, machine.getDictionaryItem("EXIT").get().getAddress());
    machine.setMemoryAt(Variables.get("HERE").getAddress(), HEREvalue + 1);
    var LATESTvalue = (Integer) machine.getMemoryAt(Variables.get("LATEST").getAddress());
    machine.pushToParameterStack(LATESTvalue);
    HIDDEN().execute(machine);
    LBRAC().execute(machine);
    EXIT().execute(machine);
  }

  @Override
  public Boolean isImmediate() {
    return true;
  }

  @Override
  public String getName() {
    return ";";
  }
}
