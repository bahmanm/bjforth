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

import static bjforth.config.Constants.BACKGROUND_COLOR;
import static bjforth.config.Constants.FOREGROUND_COLOR;
import static bjforth.primitives.PrimitiveFactory.HERE;
import static bjforth.primitives.PrimitiveFactory.WORD;
import static com.diogonunes.jcolor.Ansi.colorize;

import bjforth.machine.DictionaryItem;
import bjforth.machine.Machine;
import bjforth.machine.MachineException;

public class SEE implements Primitive {

  @Override
  public void execute(Machine machine) {
    var LITaddr = machine.getDictionaryItem("LIT").get().getAddress();
    var ZBRANCHaddr = machine.getDictionaryItem("0BRANCH").get().getAddress();
    var BRANCHaddr = machine.getDictionaryItem("BRANCH").get().getAddress();
    var HEREvalue = getHEREvalue(machine);
    var target = getTargetDictionaryItem(machine);

    printHeader(machine, target);
    var content = machine.getMemoryAt(target.getAddress());
    if (content instanceof Primitive primitive) {
      return;
    }

    var addr = target.getAddress() + 1;
    content = machine.getMemoryAt(addr);
    while (addr < target.getLength() + target.getAddress()) {
      if (content == null) {
        System.out.print(colorize("null", FOREGROUND_COLOR, BACKGROUND_COLOR));
        System.out.print(" ");
        addr += 1;
      } else if (content == LITaddr) {
        addr += 1;
        content = machine.getMemoryAt(addr);
        if (content instanceof String s) {
          System.out.print(
              colorize("\"%s\"".formatted(content), FOREGROUND_COLOR, BACKGROUND_COLOR));
          System.out.print(" ");
        } else {
          System.out.print(colorize("%s".formatted(content), FOREGROUND_COLOR, BACKGROUND_COLOR));
          System.out.print(" ");
        }
      } else if (content instanceof Integer wordAddr && ZBRANCHaddr.equals(wordAddr)) {
        addr += 1;
        content = (Integer) machine.getMemoryAt(addr);
        System.out.print(
            colorize("%s(%d)".formatted("0BRANCH", content), FOREGROUND_COLOR, BACKGROUND_COLOR));
        System.out.print(" ");
      } else if (content instanceof Integer wordAddr && BRANCHaddr.equals(wordAddr)) {
        addr += 1;
        content = (Integer) machine.getMemoryAt(addr);
        System.out.print(
            colorize("%s(%d)".formatted("BRANCH", content), FOREGROUND_COLOR, BACKGROUND_COLOR));
        System.out.print(" ");
      } else if (content instanceof Integer wordAddr) {
        var maybeItem = machine.getDictionaryItem(wordAddr);
        if (maybeItem.isEmpty()) {
          throw new MachineException("Not sure how to display value at %d08".formatted(wordAddr));
        } else {
          var item = maybeItem.get();
          System.out.print(colorize(item.getName(), FOREGROUND_COLOR, BACKGROUND_COLOR));
          System.out.print(" ");
        }
      } else {
        System.out.print(colorize(content.toString(), FOREGROUND_COLOR, BACKGROUND_COLOR));
      }
      addr += 1;
      content = machine.getMemoryAt(addr);
    }
  }

  private DictionaryItem getTargetDictionaryItem(Machine machine) {
    WORD().execute(machine);
    var word = (String) machine.popFromParameterStack();
    var maybeDictItem = machine.getDictionaryItem(word);
    if (maybeDictItem.isEmpty()) {
      throw new MachineException("No such entry.");
    } else {
      return maybeDictItem.get();
    }
  }

  private Integer getHEREvalue(Machine machine) {
    HERE().execute(machine);
    return (Integer) machine.popFromParameterStack();
  }

  private void printHeader(Machine machine, DictionaryItem target) {
    var content = machine.getMemoryAt(target.getAddress());
    if (content instanceof Primitive primitive) {
      System.out.print(
          colorize(
              "Primitive %s (%s) at %08d (immediate: %b, hidden: %b)\n"
                  .formatted(
                      primitive.getName(),
                      primitive.getDescriptiveName(),
                      target.getAddress(),
                      target.getIsImmediate(),
                      target.getIsHidden()),
              FOREGROUND_COLOR,
              BACKGROUND_COLOR));
      System.out.println();
    } else {
      System.out.print(
          colorize(
              "%s at %08d (immediate: %b, hidden: %b)"
                  .formatted(
                      target.getName().toUpperCase(),
                      target.getAddress(),
                      target.getIsImmediate(),
                      target.getIsHidden()),
              FOREGROUND_COLOR,
              BACKGROUND_COLOR));
      System.out.println();
    }
  }
}
