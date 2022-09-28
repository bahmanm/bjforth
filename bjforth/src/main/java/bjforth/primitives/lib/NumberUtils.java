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
package bjforth.primitives.lib;

import bjforth.machine.MachineException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {

  public static Number add(Number n1, Number n2) {
    if (n1 instanceof BigDecimal || n2 instanceof BigDecimal) {
      return asBigDecimal(n1).add(asBigDecimal(n2));
    } else if (n1 instanceof Double || n2 instanceof Double) {
      return asDouble(n1) + asDouble(n2);
    } else if (n1 instanceof Float || n2 instanceof Float) {
      return asFloat(n1) + asFloat(n2);
    } else if (n1 instanceof BigInteger || n2 instanceof BigInteger) {
      return asBigInteger(n1).add(asBigInteger(n2));
    } else if (n1 instanceof Long || n2 instanceof Long) {
      return asLong(n1) + asLong(n2);
    } else if (n1 instanceof Integer || n2 instanceof Integer) {
      return asInteger(n1) + asInteger(n2);
    } else if (n1 instanceof Short || n2 instanceof Short) {
      return asShort(n1) + asShort(n2);
    } else if (n1 instanceof Byte || n2 instanceof Byte) {
      return asByte(n1) + asByte(n2);
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static Number sub(Number n1, Number n2) {
    if (n1 instanceof BigDecimal || n2 instanceof BigDecimal) {
      return asBigDecimal(n1).add(asBigDecimal(n2).negate());
    } else if (n1 instanceof Double || n2 instanceof Double) {
      return asDouble(n1) - asDouble(n2);
    } else if (n1 instanceof Float || n2 instanceof Float) {
      return asFloat(n1) - asFloat(n2);
    } else if (n1 instanceof BigInteger || n2 instanceof BigInteger) {
      return asBigInteger(n1).add(asBigInteger(n2).negate());
    } else if (n1 instanceof Long || n2 instanceof Long) {
      return asLong(n1) - asLong(n2);
    } else if (n1 instanceof Integer || n2 instanceof Integer) {
      return asInteger(n1) - asInteger(n2);
    } else if (n1 instanceof Short || n2 instanceof Short) {
      return asShort(n1) - asShort(n2);
    } else if (n1 instanceof Byte || n2 instanceof Byte) {
      return asByte(n1) - asByte(n2);
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static BigDecimal asBigDecimal(Number n) {
    if (n instanceof BigDecimal bd) {
      return bd;
    } else if (n instanceof BigInteger bi) {
      return new BigDecimal(bi);
    } else if (n instanceof Double) {
      return BigDecimal.valueOf((double) n);
    } else if (n instanceof Float) {
      return BigDecimal.valueOf(asDouble(n));
    } else if (n instanceof Long
        || n instanceof Integer
        || n instanceof Short
        || n instanceof Byte) {
      return BigDecimal.valueOf((long) n);
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static Double asDouble(Number n) {
    if (n instanceof Double) {
      return (double) n;
    } else if (n instanceof Float) {
      return Double.valueOf((float) n);
    } else if (n instanceof Long
        || n instanceof Integer
        || n instanceof Short
        || n instanceof Byte) {
      return Double.valueOf((long) n);
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static Float asFloat(Number n) {
    if (n instanceof Float) {
      return (float) n;
    } else if (n instanceof Long
        || n instanceof Integer
        || n instanceof Short
        || n instanceof Byte) {
      return Float.valueOf((long) n);
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static BigInteger asBigInteger(Number n) {
    if (n instanceof BigInteger bi) {
      return bi;
    } else if (n instanceof Long
        || n instanceof Integer
        || n instanceof Short
        || n instanceof Byte) {
      return BigInteger.valueOf((long) n);
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static Long asLong(Number n) {
    if (n instanceof Long || n instanceof Integer || n instanceof Short || n instanceof Byte) {
      return (long) n;
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static Integer asInteger(Number n) {
    if (n instanceof Integer || n instanceof Short || n instanceof Byte) {
      return (int) n;
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static Short asShort(Number n) {
    if (n instanceof Short || n instanceof Byte) {
      return (short) n;
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }

  public static Byte asByte(Number n) {
    if (n instanceof Byte) {
      return (byte) n;
    } else {
      throw new MachineException("unsupported subtype of Number");
    }
  }
}
