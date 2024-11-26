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

public class DictionaryItemBuilder {

  private DictionaryItem item = null;

  private DictionaryItemBuilder() {}

  public static DictionaryItemBuilder aDictionaryItem() {
    return new DictionaryItemBuilder();
  }

  public DictionaryItemBuilder with(DictionaryItem other) {
    if (item == null) {
      item =
          new DictionaryItem(
              other.getName(), other.getAddress(), other.getIsImmediate(), other.getIsHidden());
    } else {
      item.setName(item.getName());
      item.setAddress(item.getAddress());
      item.setIsImmediate(item.getIsImmediate());
      item.setIsHidden(item.getIsHidden());
    }
    return this;
  }

  public DictionaryItemBuilder isImmediate(Boolean isImmediate) {
    item.setIsImmediate(isImmediate);
    return this;
  }

  public DictionaryItemBuilder isHidden(Boolean isHidden) {
    item.setIsHidden(isHidden);
    return this;
  }

  public DictionaryItem build() {
    return item;
  }
}
