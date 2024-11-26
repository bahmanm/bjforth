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

import java.lang.reflect.Field;
import org.apache.commons.lang3.reflect.FieldUtils;

public class PrimitiveFactoryModificationUtils {

  public static void resetPrimitive(String name) {
    try {
      var containerObject =
          FieldUtils.readStaticField(PrimitiveFactory.class, "container" + name, true);
      FieldUtils.writeField(containerObject, "instance", null, true);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void resetAllPrimitives() {
    FieldUtils.getAllFieldsList(PrimitiveFactory.class).stream()
        .map(Field::getName)
        .filter(name -> name.startsWith("container"))
        .map(name -> name.replace("container", ""))
        .forEach(PrimitiveFactoryModificationUtils::resetPrimitive);
  }
}
