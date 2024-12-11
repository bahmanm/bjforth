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

public class COLON implements Primitive {
  @Override
  public void execute(Machine machine) {
    WORD().execute(machine);
    CREATE().execute(machine);

    var LATESTvalue = (Integer) machine.getMemoryAt(Variables.get("LATEST").getAddress());
    var DOCOLaddr = machine.getDictionaryItem("DOCOL").get().getAddress();
    machine.setMemoryAt(LATESTvalue, DOCOLaddr);
    machine.setMemoryAt(Variables.get("HERE").getAddress(), LATESTvalue + 1);
    machine.pushToParameterStack(LATESTvalue);
    HIDDEN().execute(machine);
    RBRAC().execute(machine);

    machine.setMemoryAt(Variables.get("HERE").getAddress(), LATESTvalue + 1);
  }

  @Override
  public String getName() {
    return ":";
  }
}
