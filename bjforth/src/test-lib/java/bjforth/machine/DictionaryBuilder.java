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
package bjforth.machine;

public class DictionaryBuilder {

  private Dictionary dictionary = new Dictionary();

  private DictionaryBuilder() {}

  public static DictionaryBuilder aDictionary() {
    return new DictionaryBuilder();
  }

  public DictionaryBuilder with(String name, DictionaryItem item) {
    dictionary.put(name, item);
    return this;
  }

  public DictionaryBuilder with(MachineState state) {
    state
        .getDictionary()
        .items
        .forEach(
            (name, items) -> {
              items.forEach(item -> dictionary.put(name, item));
            });
    return this;
  }

  public Dictionary build() {
    return dictionary;
  }
}
