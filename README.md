# bjForth

[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/UMKeFZ8ns9T9vi5aquTfVT/FnGnFZDJWi8uY7zNYvkuvb/tree/master.svg?style=shield&circle-token=69b804abc3b70a380cfb416d80ce8d36e5ad2334)](https://dl.circleci.com/status-badge/redirect/circleci/UMKeFZ8ns9T9vi5aquTfVT/FnGnFZDJWi8uY7zNYvkuvb/tree/master)
[![codecov](https://codecov.io/gh/bahmanm/bjforth/graph/badge.svg?token=KPSFFI2H9G)](https://codecov.io/gh/bahmanm/bjforth)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/bahmanm/bjforth)
[![Matrix](https://img.shields.io/matrix/bjforth%3Amatrix.org?server_fqdn=matrix.org&style=flat&logo=matrix&color=%230e80c0)](https://matrix.to/#/#github-bahmanm-bjforth:matrix.org)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

bjForth which stands for _Bahman's Forth on the JVM_ is an attempt at writing a Forth which
would allow the programmer to tap into the JVM and its ecosystem.

The idea came to my mind in 2014-2015, but I never had the time nor the understanding of how
a Forth is implemented. 

- [Thoughts on JVM-based Forth Implementation (2014)](https://www.bahmanm.com/2015/01/more-thoughts-on-jvm-based-forth.html)
- [More Thoughts on A JVM-based Forth (2015)](https://www.bahmanm.com/2015/01/more-thoughts-on-jvm-based-forth.html)

Then in 2022, I sat down and read the source of [JONESFORTH](http://git.annexia.org/?p=jonesforth.git;a=summary).
It is a rather minimal and amasingly well-documented Forth implementation! 

After that I knew what to do!

# Roadmap

## v0.1.0

The initial version. It should remain relatively close to JONESFORTH and deviations should be minimal
and only when necessary.

## v1.0.0

Support basic JVM interop such as below:

```forth
1 2 3 @java.util.List @.of .s
\ java.util.List <3, 2, 1>

"Bar" @my.package.Foo @.new @.greet
\ Hello, Bar
```

## v2.0.0

Support lambdas and class/interface creation.

---

#### Implementation Notes
A bunch of notes which are most probably useless to anyone else but me.

##### Non-primitive words
The list of words which are defined in the assembler file (`jonesforth.S`) but are in fact
Forth words.

- COLON @ jonesforth.S:1869
- SEMICOLON @ jonesforth.S:1881
- HIDE @ jonesforth.S:1949
- TICK @ jonesforth.S:1983
