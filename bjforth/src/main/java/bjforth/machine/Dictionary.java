/*
 * Copyright 2022 Bahman Movaqar
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
package bjforth.machine;

import java.util.*;

class Dictionary {

  final Map<String, List<DictionaryItem>> items = new HashMap<>();
  final Map<Integer, String> reverseLookup = new HashMap<>();

  Dictionary() {}

  Dictionary(Dictionary other) {
    items.putAll(other.items);
    other.items.forEach(
        (name, items) -> {
          reverseLookup.putIfAbsent(items.get(0).getAddress(), name);
        });
  }

  public void put(String name, DictionaryItem item) {
    List<DictionaryItem> currentValue;
    var nameUpper = name.toUpperCase();
    if (items.containsKey(nameUpper)) {
      currentValue = items.get(nameUpper);
    } else {
      currentValue = new ArrayList<>();
    }
    currentValue.add(item);
    items.put(nameUpper, currentValue);
    reverseLookup.putIfAbsent(item.getAddress(), item.getName());
  }

  public Optional<DictionaryItem> get(String name) {
    return Optional.ofNullable(items.get(name.toUpperCase()))
        .map(dictionaryItems -> dictionaryItems.get(dictionaryItems.size() - 1));
  }

  public Optional<DictionaryItem> get(Integer address) {
    return Optional.ofNullable(reverseLookup.get(address)).flatMap(this::get);
  }
}
