# bjForth

[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/UMKeFZ8ns9T9vi5aquTfVT/FnGnFZDJWi8uY7zNYvkuvb/tree/master.svg?style=shield&circle-token=69b804abc3b70a380cfb416d80ce8d36e5ad2334)](https://dl.circleci.com/status-badge/redirect/circleci/UMKeFZ8ns9T9vi5aquTfVT/FnGnFZDJWi8uY7zNYvkuvb/tree/master)
[![codecov](https://codecov.io/gh/bahmanm/bjforth/graph/badge.svg?token=KPSFFI2H9G)](https://codecov.io/gh/bahmanm/bjforth)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/bahmanm/bjforth)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/bahmanm/bjforth/total?color=0e80c1)
[![Matrix](https://img.shields.io/matrix/bjforth%3Amatrix.org?server_fqdn=matrix.org&style=flat&logo=matrix&color=%230e80c0)](https://matrix.to/#/#github-bahmanm-bjforth:matrix.org)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fbahmanm%2Fbjforth.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fbahmanm%2Fbjforth?ref=badge_shield)

bjForth which stands for _Bahman's Forth on the JVM_ is an attempt at writing a Forth which
would allow the programmer to tap into the JVM and its ecosystem.

The idea came to my mind in 2014-2015, but I never had the time nor the understanding of how
a Forth is implemented. 

- [Thoughts on JVM-based Forth Implementation (2014)](https://www.bahmanm.com/2015/01/more-thoughts-on-jvm-based-forth.html)
- [More Thoughts on A JVM-based Forth (2015)](https://www.bahmanm.com/2015/01/more-thoughts-on-jvm-based-forth.html)

Then in 2022, I sat down and read the source of [JONESFORTH](http://git.annexia.org/?p=jonesforth.git;a=summary) which is a rather minimal and amasingly well-documented Forth implementation! 

After that I knew what to do!

# How to run

As bjForth is written with Java, all you need is JDK 21 installed.

Then to launch bjForth:

```
$ ./bjForth

bjForth <https://github.com/bahmanm/bjforth>
⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯

```

Or you could include your own library:

```
$ ./bjForth my-lib.forth

bjForth <https://github.com/bahmanm/bjforth>
⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
```

# Documentation 

* [List of all words included in the distribution](docs/Words.md)
* [Java inter-op](docs/Java%20Inter-op.md)


# Roadmap

## v1.0.0

- Java inter-op (instance creation and method call)
- A set of libraries with commonly used words (`bjForth.forth`)

## v2.0.0 (Tentative)

- ANSI Forth compatibility

## v3.0.0 (Tentative)

- Java inter-op (lambdas)

---

## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fbahmanm%2Fbjforth.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fbahmanm%2Fbjforth?ref=badge_large)
