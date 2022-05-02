/*
 * Copyright 2022 Bahman Movaqar
 *
 * This file is part of BJForth.
 *
 * BJForth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BJForth is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BJForth. If not, see <https://www.gnu.org/licenses/>.
 */
package bjforth.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class RandomUtils {

  public static Integer nextInt() {
    return org.apache.commons.lang3.RandomUtils.nextInt();
  }

  public static Integer nextIntExcluding(Integer... exclusions) {
    var n = nextInt();
    return Arrays.stream(exclusions).noneMatch(exclusion -> n == exclusion)
        ? n
        : nextIntExcluding(exclusions);
  }

  public static Long nextLong() {
    return org.apache.commons.lang3.RandomUtils.nextLong();
  }

  public static Long nextLongExcluding(Long... exclusions) {
    var n = nextLong();
    return Arrays.stream(exclusions).noneMatch(exclusion -> n == exclusion)
        ? n
        : nextLongExcluding(exclusions);
  }

  public static Float nextFloat() {
    return org.apache.commons.lang3.RandomUtils.nextFloat();
  }

  public static Float nextFloatExcluding(Float... exclusions) {
    var n = nextFloat();
    return Arrays.stream(exclusions).noneMatch(exclusion -> n == exclusion)
        ? n
        : nextFloatExcluding(exclusions);
  }

  public static Double nextDouble() {
    return org.apache.commons.lang3.RandomUtils.nextDouble();
  }

  public static Double nextDoubleExcluding(Double... exclusions) {
    var n = nextDouble();
    return Arrays.stream(exclusions).noneMatch(exclusion -> n == exclusion)
        ? n
        : nextDoubleExcluding(exclusions);
  }

  public static Short nextShort() {
    var n = org.apache.commons.lang3.RandomUtils.nextInt(0, Short.MAX_VALUE - 1);
    return (short) (org.apache.commons.lang3.RandomUtils.nextBoolean() ? n : -n);
  }

  public static Short nextShortExcluding(Short... exclusions) {
    var n = nextShort();
    return Arrays.stream(exclusions).noneMatch(exclusion -> n == exclusion)
        ? n
        : nextShortExcluding(exclusions);
  }

  public static BigDecimal nextBigDecimal() {
    return BigDecimal.valueOf(nextDouble());
  }

  public static BigDecimal nextBigDecimalExcluding(List<BigDecimal> exclusions) {
    var n = nextBigDecimal();
    return exclusions.stream().noneMatch(exclusion -> exclusion.equals(n))
        ? n
        : nextBigDecimalExcluding(exclusions);
  }

  public static BigDecimal nextBigDecimalExcluding(Double... exclusions) {
    return nextBigDecimalExcluding(Arrays.stream(exclusions).map(BigDecimal::valueOf).toList());
  }

  public static BigDecimal nextBigDecimalExcluding(Integer... exclusions) {
    return nextBigDecimalExcluding(Arrays.stream(exclusions).map(BigDecimal::valueOf).toList());
  }

  public static Byte nextByte() {
    return org.apache.commons.lang3.RandomUtils.nextBytes(1)[0];
  }

  public static Byte nextByteExcluding(Byte... exclusions) {
    var n = nextByte();
    return Arrays.stream(exclusions).noneMatch(exclusion -> n == exclusion)
        ? n
        : nextByteExcluding(exclusions);
  }

  public static BigInteger nextBigInteger() {
    return BigInteger.valueOf(nextLong());
  }

  public static BigInteger nextBigInteger(List<BigInteger> exclusions) {
    var n = nextBigInteger();
    return exclusions.stream().noneMatch(exclusion -> exclusion.equals(n))
        ? n
        : nextBigInteger(exclusions);
  }

  public static BigInteger nextBigIntegerExcluding(Integer... exclusions) {
    return nextBigInteger(Arrays.stream(exclusions).map(BigInteger::valueOf).toList());
  }
}
