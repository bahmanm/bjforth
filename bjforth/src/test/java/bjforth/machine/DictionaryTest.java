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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DictionaryTest {

  @DisplayName("#get(String) should do a case-insensitive search")
  @Test
  void getByNameCaseInsensitive() {
    // GIVEN
    var dict = new Dictionary();
    var name = RandomStringUtils.insecure().next(20);
    var dictItem = new DictionaryItem(name, 0, false, false);
    dict.put(name, dictItem);

    // EXPECT
    assertThat(dict.get(name.toLowerCase())).isPresent().hasValueSatisfying(dictItem::equals);
    assertThat(dict.get(name.toUpperCase())).isPresent().hasValueSatisfying(dictItem::equals);
  }

  @DisplayName("#get(Integer) should do a case-insensitive search")
  @Test
  void getByAddressCaseInsensitive() {
    // GIVEN
    var dict = new Dictionary();
    var name = RandomStringUtils.insecure().next(20);
    var dictItem = new DictionaryItem(name, 0, false, false);
    dict.put(name, dictItem);

    // EXPECT
    assertThat(dict.get(0)).isPresent().hasValueSatisfying(dictItem::equals);
  }

  @DisplayName("#getAllForName(String) should do a case-insensitive search")
  @Test
  void getAllForNameCaseInsensitive() {
    // GIVEN
    var dict = new Dictionary();
    var name = RandomStringUtils.insecure().next(20);
    var dictItem = new DictionaryItem(name, 0, false, false);
    dict.put(name, dictItem);

    // EXPECT
    assertThat(dict.getAllForName(name.toLowerCase()))
        .isPresent()
        .hasValueSatisfying(v -> List.of(dictItem).equals(v));
    assertThat(dict.getAllForName(name.toUpperCase()))
        .isPresent()
        .hasValueSatisfying(v -> List.of(dictItem).equals(v));
  }

  @DisplayName("#put should add the item to the tip of the list")
  @Test
  void putAddFirst() {
    // GIVEN
    var dict = new Dictionary();
    var name = RandomStringUtils.insecure().next(20);
    var dictItem1 = new DictionaryItem(name, 0, false, false);
    dict.put(name, dictItem1);
    var dictItem2 = new DictionaryItem(name, 1, false, false);
    dict.put(name, dictItem2);

    // EXPECT
    assertThat(dict.getAllForName(name))
        .isPresent()
        .hasValueSatisfying(v -> List.of(dictItem2, dictItem1).equals(v));
  }

  @DisplayName("#remove should remove the item both from `items` and `reverseLookup`.")
  @Test
  void remove() {
    // GIVEN
    var dict = new Dictionary();
    var name = RandomStringUtils.insecure().next(20);
    var dictItem1 = new DictionaryItem(name, 0, false, false);
    dict.put(name, dictItem1);
    var dictItem2 = new DictionaryItem(name, 1, false, false);
    dict.put(name, dictItem2);

    // WHEN
    dict.remove(name);

    // THEN
    assertThat(dict.getAllForName(name))
        .isPresent()
        .hasValueSatisfying(v -> List.of(dictItem1).equals(v));
  }
}
