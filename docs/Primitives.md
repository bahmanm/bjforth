# Primitives

Primitives in bjForth are words which are implemented in Java.

In the following table, which lists all the primitives

- `x`, `y` and `z` denote numbers
- `p` and `q` denote a pointer/address
- `o` denotes an object
- `a`, `b`, `c`, ... denote values of any type
- `...` denotes variable size input


| Name        | Stack effect       | Long name      | Notes                                                      |
|-------------|--------------------|----------------|------------------------------------------------------------|
| `+`         | `y       - z`      | ADD            |                                                            |
| `+!`        | `p x     - `       | ADDSTORE       |                                                            |
| `@@`        | `... a b - o`      | ATAT           | Create a new object, `new/0 java.lang.Object @@`           |
| `BASE`      | `        - x`      |                |                                                            |
| `BRANCH`    | `        -`        |                |                                                            |
| `BYE`       | `        -`        |                |                                                            |
| `>CFA`      | `p       - `       |                |                                                            |
| `CHAR`      | `        - x`      |                |                                                            |
| `:`         | `        - p`      | COLON          |                                                            |
| `,,`        | `... a b - o`      | COMMACOMMA     | Call a static method,`10 valueOf/1 java.lang.String ,,`    |
| `,`         | `a       -`        | COMMA          |                                                            |
| `COPY`      | `p q     -`        |                |                                                            |
| `CREATE`    | `a       -`        |                |                                                            |
| `4-`        | `x       - y`      | DECR4          |                                                            |
| `-`         | `x       - y`      | DECR           |                                                            |
| `>DFA`      | `p       - q`      |                |                                                            |
| `/`         | `x y     - z`      | DIV            |                                                            |
| `..`        | `... a b - o`      | DOTDOT         | Call an instance method, `10 longValue/0 ..`               |
| `."`        | `        - o`      | DOTDOUBLEQUOTE | Create a string literal `." Hello, world ".                |
| `.S`        | `        -`        | DOTSTACK       |                                                            |
| `DROP`      | `a       -`        | DROP           |                                                            |
| `DSP@`      | `        - x`      | DSPFETCH       |                                                            |
| `DSP!`      | `x       -`        | DSPSTORE       |                                                            |
| `DUP`       | `a       - a a`    |                |                                                            |
| `EMIT`      | `a       -`        |                |                                                            |
| `=`         | `a b     - x`      | EQU            |                                                            |
| `EXECUTE`   | `a       -`        |                |                                                            |
| `EXIT`      | `a       -`        |                |                                                            |
| `@`         | `p       - a`      |                |                                                            |
| `FIND`      | `a       - p`      |                |                                                            |
| `R>`        | `R a     - P a`    | FROMR          |                                                            |
| `>=`        | `a b     - x`      | GE             |                                                            |
| `>`         | `a b     - x`      | GT             |                                                            |
| `HERE`      | `        - p`      |                |                                                            |
| `HIDDEN`    | `        -`        |                |                                                            |
| `HIDE`      | `        -`        |                |                                                            |
| `IMMEDIATE` | `        -`        |                |                                                            |
| `4+`        | `x       - y`      | INCR4          |                                                            |
| `1+`        | `x       - y`      | INCR           |                                                            |
| `INTERPRET` | `a       - b`      |                |                                                            |
| `KEY`       | `        - a`      |                |                                                            |
| `LATEST`    | `        - p`      |                |                                                            |
| `[`         | `        -`        | LBRAC          |                                                            |
| `LIT`       | `        -`        |                |                                                            |
| `LITSTRING` | `        - a `     |                |                                                            |
| `<=`        | `a b     - x`      | LTE            |                                                            |
| `<`         | `a b     - x`      | LT             |                                                            |
| `MOD`       | `x y     - z`      |                |                                                            |
| `MOVE`      | `x p q   -`        |                |                                                            |
| `*`         | `x y     - z`      | MUL            |                                                            |
| `<>`        | `x y     - z`      | NEQU           |                                                            |
| `-ROT`      | `a b c   - c b a`  | NROT           |                                                            |
| `NUMBER`    | `        - x`      |                |                                                            |
| `OVER`      | `a b     - a b a`  |                |                                                            |
| `PRINT`     | `a       - a`      |                |                                                            |
| `PRINTLN`   | `a       - a`      |                |                                                            |
| `?DUP`      | `a       - a a`    | QDUP           |                                                            |
| `QUIT`      | `        -`        | QUIT           |                                                            |
| `]`         | `        -`        | RBRAC          |                                                            |
| `RDROP`     | `R p     -`        |                |                                                            |
| `ROT`       | `a b c   - b c a`  |                |                                                            |
| `RSP@`      | `        -`        | RSPFETCH       |                                                            |
| `RSP!`      | `x       -`        | RSPSTORE       |                                                            |
| `;`         | `        - q`      | SEMICOLON      |                                                            |
| `!``        | `a p     -`        | STORE          |                                                            |
| `-`         | `x y     - z`      | SUB            |                                                            |
| `-!`        | `p x     -`        | SUBSTORE       |                                                            |
| `SWAP`      | `a b     - b a`    |                |                                                            |
| `TELL`      | `o p x   -`        |                |                                                            |
| `'`         | `        - p`      | TICK           |                                                            |
| `>R`        | `a       - R a`    | TOR            |                                                            |
| `2DROP`     | `a b     -`        | TWODROP        |                                                            |
| `2DUP`      | `a b     - a b a b`| TWODUP         |                                                            |
| `2SWAP`     | `a b c d - c d a b`| TWOSWAP        |                                                            |
| `WORD`      | `        - a`      | WORD           |                                                            |
| `0BRANCH`   | `x       -`        | ZBRANCH        |                                                            |
| `0=`        | `x       -`        | ZEQU           |                                                            |
| `0>=`       | `x       -`        | ZGE            |                                                            |
| `0>`        | `x       -`        | ZGT            |                                                            |
| `0<=`       | `x       -`        | ZLE            |                                                            |
| `0<`        | `x       -`        | ZLT            |                                                            |
| `0<>`       | `x       -`        | ZNEQU          |                                                            |
              
