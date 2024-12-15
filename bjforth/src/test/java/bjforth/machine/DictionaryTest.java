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

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DictionaryTest {

  @DisplayName("#get() should do a case-insensitive search")
  @Test
  void getCaseInsensitive() {
    // GIVEN
    var dict = new Dictionary();
    var name = RandomStringUtils.insecure().next(20);
    var dictItem = new DictionaryItem(name, 0, false, false);
    dict.put(name, dictItem);

    // EXPECT
    assertThat(dict.get(name.toLowerCase())).isPresent().hasValueSatisfying(dictItem::equals);
    assertThat(dict.get(name.toUpperCase())).isPresent().hasValueSatisfying(dictItem::equals);
  }
}
