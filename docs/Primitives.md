# Primitives

Primitives in bjForth are words which are implemented in Java.

In the following table, which lists all the primitives

- `x`, `y` and `z` denote numbers
- `p` and `q` denote a pointer/address
- `o` denotes an object
- `a`, `b`, `c`, ... denote values of any type
- `...` denotes variable size input


| Name        | Stack effect        | Long name      | Notes                                            |
|-------------|---------------------|----------------|--------------------------------------------------|
| `+`         | `y       - z`       | ADD            |                                                  |
| `+!`        | `p x     - `        | ADDSTORE       |                                                  |
| `AND`       | `x y     - z`       |                |                                                  |
| `@@`        | `... a b - o`       | ATAT           | Create a new object, `new/0 java.lang.Object @@` |
| `BASE`      | `        - x`       |                |                                                  |
| `BRANCH`    | `        -`         |                |                                                  |
| `BYE`       | `        -`         |                |                                                  |
| `>CFA`      | `p       - `        |                |                                                  |
| `CHAR`      | `        - x`       |                |                                                  |
| `:`         | `        - p`       | COLON          |                                                  |
| `@<`        | `s - o`             | ATLANGLE       | Parses the spec of a constructor                 |
| `,<`        | `s - o`             | COMMALAGNLE    | Parses the spec of a static method               |
| `,`         | `a       -`         | COMMA          |                                                  |
| `COPY`      | `p q     -`         |                |                                                  |
| `CREATE`    | `a       -`         |                |                                                  |
| `4-`        | `x       - y`       | DECR4          |                                                  |
| `-`         | `x       - y`       | DECR           |                                                  |
| `>DFA`      | `p       - q`       |                |                                                  |
| `/`         | `x y     - z`       | DIV            |                                                  |
| `.<`        | `s - o`             | DOTLANGLE      | Parses the spec of an instance method,           |
| `."`        | `        - o`       | DOTDOUBLEQUOTE | Create a string literal `." Hello, world ".      |
| `.S`        | `        -`         | DOTSTACK       |                                                  |
| `DROP`      | `a       -`         | DROP           |                                                  |
| `DSP@`      | `        - x`       | DSPFETCH       |                                                  |
| `DSP!`      | `x       -`         | DSPSTORE       |                                                  |
| `DUP`       | `a       - a a`     |                |                                                  |
| `EMIT`      | `a       -`         |                |                                                  |
| `=`         | `a b     - x`       | EQU            |                                                  |
| `EXECUTE`   | `a       -`         |                |                                                  |
| `EXIT`      | `a       -`         |                |                                                  |
| `@`         | `p       - a`       |                |                                                  |
| `FIND`      | `a       - p`       |                |                                                  |
| `FORGTET`   | `        - `        |                |                                                  |
| `R>`        | `R a     - P a`     | FROMR          |                                                  |
| `>=`        | `a b     - x`       | GE             |                                                  |
| `>`         | `a b     - x`       | GT             |                                                  |
| `HERE`      | `        - p`       |                |                                                  |
| `HIDDEN`    | `        -`         |                |                                                  |
| `HIDE`      | `        -`         |                |                                                  |
| `IMMEDIATE` | `        -`         |                |                                                  |
| `ID.`       | `x       -`         | IDDOT          |                                                  |
| `4+`        | `x       - y`       | INCR4          |                                                  |
| `1+`        | `x       - y`       | INCR           |                                                  |
| `INTERPRET` | `a       - b`       |                |                                                  |
| `KEY`       | `        - a`       |                |                                                  |
| `LATEST`    | `        - p`       |                |                                                  |
| `[`         | `        -`         | LBRAC          |                                                  |
| `LIT`       | `        -`         |                |                                                  |
| `LITSTRING` | `        - a `      |                |                                                  |
| `<=`        | `a b     - x`       | LTE            |                                                  |
| `<`         | `a b     - x`       | LT             |                                                  |
| `MOD`       | `x y     - z`       |                |                                                  |
| `MOVE`      | `x p q   -`         |                |                                                  |
| `*`         | `x y     - z`       | MUL            |                                                  |
| `<>`        | `x y     - z`       | NEQU           |                                                  |
| `-ROT`      | `a b c   - c b a`   | NROT           |                                                  |
| `NUMBER`    | `        - x`       |                |                                                  |
| `NULL`      | `        - null`    |                |                                                  |
| `OR`        | ` x y    - z`       |                |                                                  |
| `OVER`      | `a b     - a b a`   |                |                                                  |
| `PRINT`     | `a       - a`       |                |                                                  |
| `PRINTLN`   | `a       - a`       |                |                                                  |
| `?DUP`      | `a       - a a`     | QDUP           |                                                  |
| `?NULL`     | `a       - x        | QNULL          |                                                  |
| `QUIT`      | `        -`         | QUIT           |                                                  |
| `]`         | `        -`         | RBRAC          |                                                  |
| `RDROP`     | `R p     -`         |                |                                                  |
| `ROT`       | `a b c   - b c a`   |                |                                                  |
| `RSP@`      | `        -`         | RSPFETCH       |                                                  |
| `RSP!`      | `x       -`         | RSPSTORE       |                                                  |
| `;`         | `        - q`       | SEMICOLON      |                                                  |
| `!`         | `a p     -`         | STORE          |                                                  |
| `!BASE`     | `x       -`         | STOREBASE      |                                                  |
| `!HERE`     | `x       -`         | STOREHERE      |                                                  |
| `-`         | `x y     - z`       | SUB            |                                                  |
| `-!`        | `p x     -`         | SUBSTORE       |                                                  |
| `SWAP`      | `a b     - b a`     |                |                                                  |
| `TELL`      | `o p x   -`         |                |                                                  |
| `'`         | `        - p`       | TICK           |                                                  |
| `>@`        | `a b ... c - o`     | RANGLEAT       | Executes a constructor (`@<`)                    |
| `>,`        | `a b ... c - o`     | RANGLECOMMA    | Executes a static method (`,<`)                  |
| `>.`        | `a b ... c - o`     | RANGLEDOT      | Executes an instance method (`.<`)               |
| `>R`        | `a       - R a`     | TOR            |                                                  |
| `2DROP`     | `a b     -`         | TWODROP        |                                                  |
| `2DUP`      | `a b     - a b a b` | TWODUP         |                                                  |
| `2SWAP`     | `a b c d - c d a b` | TWOSWAP        |                                                  |
| `WORD`      | `        - a`       | WORD           |                                                  |
| `0BRANCH`   | `x       -`         | ZBRANCH        |                                                  |
| `0=`        | `x       -`         | ZEQU           |                                                  |
| `0>=`       | `x       -`         | ZGE            |                                                  |
| `0>`        | `x       -`         | ZGT            |                                                  |
| `0<=`       | `x       -`         | ZLE            |                                                  |
| `0<`        | `x       -`         | ZLT            |                                                  |
| `0<>`       | `x       -`         | ZNEQU          |                                                  |
              
