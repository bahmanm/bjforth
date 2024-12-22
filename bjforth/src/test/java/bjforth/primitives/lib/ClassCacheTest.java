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
package bjforth.primitives.lib;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.junit.jupiter.api.Test;

class ClassCacheTest {

  @Test
  void worksOkFullyQualified() {
    // GIVEN
    var className = "java.lang.String";

    // EXPECT
    assertThat(ClassCache.forName(className)).isEqualTo(String.class);
  }

  @Test
  void worksOkUnqualified() {
    // GIVEN
    var className = "File";

    // EXPECT
    assertThat(ClassCache.forName(className)).isEqualTo(File.class);
  }

  @Test
  void worksOkVariadic() {
    // GIVEN
    var className = "Object";

    // EXPECT
    assertThat(ClassCache.forNameVararg(className)).isEqualTo(Object[].class);
  }
}
