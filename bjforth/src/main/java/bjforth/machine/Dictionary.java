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
          reverseLookup.putIfAbsent(items.getFirst().getAddress(), name);
        });
  }

  public void put(String name, DictionaryItem item) {
    var nameUpper = name.toUpperCase();
    items.computeIfAbsent(nameUpper, k -> new ArrayList<>()).addFirst(item);
    reverseLookup.putIfAbsent(item.getAddress(), item.getName());
  }

  public Optional<DictionaryItem> get(String name) {
    return Optional.ofNullable(items.get(name.toUpperCase())).map(List::getLast);
  }

  public Optional<DictionaryItem> get(Integer address) {
    return Optional.ofNullable(reverseLookup.get(address))
        .flatMap(
            name -> {
              return Optional.ofNullable(items.get(name.toUpperCase()))
                  .flatMap(
                      dictItems ->
                          dictItems.stream()
                              .filter(item -> item.getAddress().equals(address))
                              .findFirst());
            });
  }

  public Optional<List<DictionaryItem>> getAllForName(String name) {
    return Optional.ofNullable(items.get(name.toUpperCase()));
  }

  public void remove(String name) {
    var item = items.get(name);
    if (item != null) {
      reverseLookup.remove(item.getFirst().getAddress());
      item.removeFirst();
      if (item.isEmpty()) {
        items.remove(name);
      }
    }
  }

  public Set<String> getNames() {
    return items.keySet();
  }
}
