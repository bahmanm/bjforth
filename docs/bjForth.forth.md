# Words defined in the `bjForth.forth` library
_Automatically generated from the source._

### Table of contents

  * `\n ( -- s )`
  * `CR ( -- )`
  * `BL ( -- s )`
  * `SPACE ( -- )`
  * `NEGATE ( a -- b )`
  * `TRUE ( -- 1 )`
  * `FALSE ( -- 0 )`
  * `NOT ( b -- a )`
  * `[COMPILE] ( -- )`
  * `IF`
  * `THEN`
  * `ELSE`
  * `UNLESS`
  * `BEGIN`
  * `UNTIL`
  * `AGAIN`
  * `WHILE`
  * `REPEAT`
  * `NIP ( x y -- y )`
  * `TUCK ( x y -- y x y )`
  * `PICK ( x_u ... x_1 x_0 u -- x_u ... x_1 x_0 x_u )`
  * `(`
  * `/MOD ( x y - quotient remainder )`
  * `SPACES ( n -- )`
  * `DECIMAL ( -- )`
  * `HEX ( -- )`
  * `STR-PADDED-FORMAT ( padding -- format-string )`
  * `STR-REPLACE ( replacement target s -- s )`
  * `NUM->STR ( x width -- )`
  * `. ( x -- )`
  * `FILE-OPEN ( s -- BufferedReader )`
  * `FILE-READ-LINE ( BufferedReader -- s )`
  * `FILE-PRINT ( BufferedReader -- )`
  * `FILE-CLOSE ( BufferedReader -- )`
  * `MAX ( x y -- max )`
  * `MIN ( x y -- mIN )`
  * `SQRT ( x -- square-root )`
  * `WITHIN ( c a b -- boolean )`
  * `x CONSTANT name ( x -- )`
  * `ALLOT ( n -- addr )`
  * `dummy-value VARIABLE name ( x -- )`
  * `CASE test1 OF ... ENDOF test2 OF ... ENDOF ENDCASE`
  * `:ANON ( -- xt )`
  * `['] ( -- )`---

#### `\n ( -- s )`

New line constant.

---

#### `CR ( -- )`
Prints a new line.

---

#### `BL ( -- s )`

Whitespace constant.

---

#### `SPACE ( -- )`

Prints a whitespace

---

#### `NEGATE ( a -- b )`

Negates the top of stack.

---

#### `TRUE ( -- 1 )`

Truthy constant.

---

#### `FALSE ( -- 0 )`

Truthy constant.

---

#### `NOT ( b -- a )`

Logically negates the truthy value at the top of stack.

---

#### `[COMPILE] ( -- )`

Compiles a word in compiling word as if it were in immediate mode.

---

#### `IF`

Basic conditional: `IF ... ELSE ... THEN`

```forth
: IS-10?
  10 = IF
    ." It's 10! ". PRINTLN
   ELSE
     ." Nope ". PRINTLN
   THEN
;
10 IS-10?
It's 10!
20 IS-10?
Nope
```

---

#### `THEN`

---

#### `ELSE`

---

#### `UNLESS`

Basic conditional.

```forth
: IS-10?
  10 = UNLESS
    ." Nope ". PRINTLN
  ELSE
    ." It's 10 ". PRINTLN
  THEN
;
10 IS-10?
It's 10
20 IS-10?
Nope
```

---

#### `BEGIN`

---

#### `UNTIL`

Basic looping construct.

```forth
: COUNT-TO-9
 1
 BEGIN
 PRINT SPACE
 1+
 DUP 10 = UNTIL
;
COUNT-TO-9
1 2 3 4 5 6 7 8 9
```

---

#### `AGAIN`

An infinite loop that can be only returned from via `EXIT`.

```forth
: COUNT-TO-9
  1
  BEGIN
  PRINT SPACE
  1+
  DUP 10 = IF
    EXIT
  THEN
  AGAIN
;
COUNT-TO-9
1 2 3 4 5 6 7 8 9
```forth

---

#### `WHILE`

Basic looping construct.

BEGIN condition WHILE loop-part REPEAT

```forth
: COUNT-TO-9
1
BEGIN
  DUP 10 <>
WHILE
  PRINT SPACE
  1+
REPEAT
;
COUNT-TO-9
1 2 3 4 5 6 7 8 9
```

---

#### `REPEAT`

---

#### `NIP ( x y -- y )`

Drops the 2nd element of the stack.

```forth
10 20 NIP .S
java.lang.Integer<20>
```

---

#### `TUCK ( x y -- y x y )`

Makes a copy of the top of the stack as the 3rd top of stack.

```forth
10 20 TUCK .S
java.lang.Integer<20>
java.lang.Integer<10>
java.lang.Integer<20>
```

---

#### `PICK ( x_u ... x_1 x_0 u -- x_u ... x_1 x_0 x_u )`

Copies the `u`th element of the stack to the top.

```forth
10 20 30 40 1 PICK
.S
java.lang.Integer<10>
java.lang.Integer<20>
java.lang.Integer<30>
java.lang.Integer<40>
java.lang.Integer<20>
```

---

#### `(`

Immediate word that treats `(` and `)` as comment indicators.

---

#### `/MOD ( x y - quotient remainder )`

Leaves the quotient and remainder of `y / x` on the stack.

---

#### `SPACES ( n -- )`

Prints `n` whitespaces.

---

#### `DECIMAL ( -- )`

Sets the value of `BASE` to 10.

---

#### `HEX ( -- )`

Sets the value of `BASE` to 16.

---

#### `STR-PADDED-FORMAT ( padding -- format-string )`

Produces a format string which left pads a string to `padding`.

```forth
10 STR-PADDED-FORMAT PRINT
%10s
```

---

#### `STR-REPLACE ( replacement target s -- s )`

Replaces all occurrences of `target` in `s` with `replacement`.

```forth
." BOO ". ." ll ". ." Hello ". STR-REPLACE PRINT
HeBOOo

---

#### `NUM->STR ( x width -- )`

Prints a given number to string representation in the same base as `BASE`.
The result is padded with zeros to the width specified.

```forth
224 4 HEX NUM->STR
00e0
```

---

#### `. ( x -- )`

Prints a given number to string representation in the same base as `BASE`.

```forth
DECIMAL 220 HEX .
dc
```

---

#### `FILE-OPEN ( s -- BufferedReader )`

```forth
." /home/bahman/.bashrc ". FILE-OPEN
.S
java.io.BufferedReader<java.io.BufferedReader@2ac1fdc4>
```

---

#### `FILE-READ-LINE ( BufferedReader -- s )`

Reads a single line from a file.

```forth
FILE-READ-LINE
.S
java.lang.String<# -*- mode: sh-mode; sh-basic-offset: 2; -*->
```

---

#### `FILE-PRINT ( BufferedReader -- )`

Prints the contents of a file to stdout.

---

#### `FILE-CLOSE ( BufferedReader -- )`

Closes the file handle.

---

#### `MAX ( x y -- max )`

Pushes the larger number onto stack.

```forth
10 20 MAX .
20
```

---

#### `MIN ( x y -- mIN )`

Pushes the larger number onto stack.

```forth
10 20 MIN .
10
```

---

#### `SQRT ( x -- square-root )`

```forth
10 SQRT .S
java.lang.Double: 3.1622776601683795
```

---

#### `WITHIN ( c a b -- boolean )`

c a b WITHIN returns true if  and b > c and a <= c

```forth
20 30 10 WITHIN .
1
```

---

#### `x CONSTANT name ( x -- )`

Creates a constant named `name` with the value at the top of stack.

```forth
10 CONSTANT FOO
FOO .
10
```

---

#### `ALLOT ( n -- addr )`

Allocates `n` memory cells (increments `HERE` by `n`).

---

#### `dummy-value VARIABLE name ( x -- )`

Creates a variable accessible via `!` and `@` words.
```forth
-1 VARIABLE FOO
10 FOO !
FOO @ .
10
```

---

#### `CASE test1 OF ... ENDOF test2 OF ... ENDOF ENDCASE`

```forth
: 5-OR-10
  CASE
    5 OF
      ." It's 5! ". PRINT
    ENDOF
    10 OF
      ." It's 10! ". PRINT
    ENDOF
  ENDCASE
;
```

---

#### `:ANON ( -- xt )`

Creates an anonymous word and pushes its XT on stack (which can be run via `EXECUTE`.)

```forth
:ANON ." Hello, world ". PRINTLN ;
EXECUTE
Hello, world
```

---

#### `['] ( -- )`

Compiles the XT of the next word on the spot and places it in memory alongside `LIT`.

```forth
: FOO 10 20 * ;
: BAR ['] FOO ;
SEE BAR
BAR at 00000516 (immediate: false, hidden: false)
509
```


