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
package bjforth.config;

import static com.diogonunes.jcolor.Attribute.BACK_COLOR;
import static com.diogonunes.jcolor.Attribute.TEXT_COLOR;

import com.diogonunes.jcolor.Attribute;

public class Constants {

  public static final String HELLO_EMOJI = "\uD83D\uDC4B";
  public static final String BYE_EMOJI = "\uD83D\uDC4B";
  public static final String ERROR_EMOJI = "⛔";
  public static final String WARN_EMOJI = "⚠\uFE0F";
  public static final String INFO_EMOJI = "\uD83D\uDCA1";

  public static final Attribute BACKGROUND_COLOR = BACK_COLOR(254);
  public static final Attribute FOREGROUND_COLOR = TEXT_COLOR(235);
}
