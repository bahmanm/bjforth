# Java Inter-op

bjForth provides three pairs of words (primitives) to tap into the power of JVM.

## Constructors

The `@<` primitive parses the specification of the constructor and `>@` executes the given constructor and pushes the 
result onto ParameterStack.

The specification for the constructor consists of 3 distinct pieces:
- The class. It can be a fully qualified type name or in case the class resides in any of `java.lang`, `java.util` or
  `java.io` packages it can be a short (simple) name.
- Parameter types. The types are treated in the same way as the class above.
- Arity. It denotes how many elements from ParameterStack should be popped.

_NOTE 1: The parameter types must be identical to those of the constructor declaration._
_NOTE 2: There is an arbitrary limit to the number of non-variadic arguments for a variadic method call. It's 7! Note 
that this doesn't apply to non-variadic methods!_

### Examples

- Create a string (pretty useless though as strings are native to bjForth via `."` and `".` words.

```forth

." Hello, world ". @< String(String)/1

```

- Constructor with a variadic argument

```java
package mypackage.other;

import mypackage.Bar;

public class Foo {
  public Foo(String, File, Bar...);   
}

```

```forth

<a Bar instance> <another Bar instance> <a File> <a String> @< mypackage.other.Foo(String, File, mypackage.Bar...)/4 >@

```

## Instance methods

The `.<` parses the specification of the constructor and `>.` executes the given method and pushes the
result onto ParameterStack.

The specification for the constructor is composed of 2 distinct pieces:
- Parameter types. The types are treated in the same way as the the class above.
- Arity. It denotes how many elements from ParameterStack should be popped.

_NOTE: The parameter types must be identical to those of the constructor declaration._

### Examples

- Get the Long value of a given integer.

```forth

24 .< longValue()/0 >.

```

- Method with a variadic argument

```java
package mypackage.other;

import mypackage.Bar;

public class Foo {
  public foo(String, File, Bar...);   
}

```

```forth

<a Bar instance> <another Bar instance> <a File> <a String> <a Foo instance> .< foo(String, File, mypackage.Bar...)/4 >.

```

## Static methods

The `,<` primitive parses the specification of the static method and `>,` executes the given method and pushes the
result onto ParameterStack.

The specification for the constructor consists of 3 distinct pieces:
- The class. It can be a fully qualified type name or in case the class resides in any of `java.lang`, `java.util` or
  `java.io` packages it can be a short (simple) name.
- The method name.
- Parameter types. The types are treated in the same way as the class above.
- Arity. It denotes how many elements from ParameterStack should be popped.

_NOTE: The parameter types must be identical to those of the constructor declaration._

### Examples

- Get the Integer value of a String.

```forth

." 12 ". ,< Integer/valueOf(String)/1 >,

```

- Method with a variadic argument

```forth

." world ". ." Hello ". ." ,  ". ,< String/join(CharSequence, CharSequence...)/3 >,

```

