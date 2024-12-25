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
import java.util.NoSuchElementException;

class FIND implements Primitive {

  @Override
  public void execute(Machine machine) {
    try {
      var wordNameObject = machine.popFromParameterStack();
      if (wordNameObject instanceof String wordName) {
        var maybeDictItems = machine.getDictionaryItems(wordName);
        if (maybeDictItems.isPresent()) {
          var maybeDictItem =
              maybeDictItems.get().stream().filter(dictItem -> !dictItem.getIsHidden()).findFirst();
          maybeDictItem.ifPresentOrElse(
              (dictionaryItem) -> {
                if (!dictionaryItem.getIsHidden()) {
                  var dictionaryItemAddress = dictionaryItem.getAddress();
                  machine.pushToParameterStack(dictionaryItemAddress);
                }
              },
              () -> {
                throw new MachineException("No such DictionaryItem: %s".formatted(wordName));
              });
        } else {
          throw new MachineException(
              "No such DictionaryItem: %s".formatted(wordNameObject.toString()));
        }
      } else {
        throw new MachineException("Invalid argument");
      }
    } catch (NoSuchElementException ex) {
      throw new MachineException("ParameterStack error.");
    }
  }
}
