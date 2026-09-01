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
    return switch (n) {
      case BigDecimal bd -> bd;
      case BigInteger bi -> new BigDecimal(bi);
      case Double d -> BigDecimal.valueOf(d);
      case Float f -> BigDecimal.valueOf(asDouble(f));
      case Long l -> BigDecimal.valueOf(l);
      case Integer i -> BigDecimal.valueOf(i.longValue());
      case Short s -> BigDecimal.valueOf(s.longValue());
      case Byte b -> BigDecimal.valueOf(b.longValue());
      default -> throw new MachineException("unsupported subtype of Number");
    };
  }

  public static Double asDouble(Number n) {
    return switch (n) {
      case Double d -> d;
      case Float f -> (double) f;
      case Long l -> (double) l;
      case Integer i -> (double) i;
      case Short s -> (double) s;
      case Byte b -> (double) b;
      default -> throw new MachineException("unsupported subtype of Number");
    };
  }

  public static Float asFloat(Number n) {
    return switch (n) {
      case Float f -> f;
      case Long l -> (float) l;
      case Integer i -> (float) i;
      case Short s -> (float) s;
      case Byte b -> (float) b;
      default -> throw new MachineException("unsupported subtype of Number");
    };
  }

  public static BigInteger asBigInteger(Number n) {
    return switch (n) {
      case BigInteger bi -> bi;
      case Long l -> BigInteger.valueOf(l);
      case Integer i -> BigInteger.valueOf(i.longValue());
      case Short s -> BigInteger.valueOf(s.longValue());
      case Byte b -> BigInteger.valueOf(b.longValue());
      default -> throw new MachineException("unsupported subtype of Number");
    };
  }

  public static Long asLong(Number n) {
    return switch (n) {
      case Long l -> l;
      case Integer i -> i.longValue();
      case Short s -> s.longValue();
      case Byte b -> b.longValue();
      default -> throw new MachineException("unsupported subtype of Number");
    };
  }

  public static Integer asInteger(Number n) {
    return switch (n) {
      case Integer i -> i;
      case Short s -> s.intValue();
      case Byte b -> b.intValue();
      default -> throw new MachineException("unsupported subtype of Number");
    };
  }

  public static Short asShort(Number n) {
    return switch (n) {
      case Short s -> s;
      case Byte b -> (short) b.byteValue();
      default -> throw new MachineException("unsupported subtype of Number");
    };
  }

  public static Byte asByte(Number n) {
    return switch (n) {
      case Byte b -> b;
      default -> throw new MachineException("unsupported subtype of Number");
    };
  }
}
