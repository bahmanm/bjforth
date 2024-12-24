### Defining words
: MULTIPLY * ;
: TIMES-10 10 MULTIPLY ;
20 TIMES-10 PRINTLN
50 50 + PRINTLN

### Strings
." Hello, world ". PRINTLN
: PRINT-ME ." Hello, world ". PRINTLN ;
PRINT-ME

### Java inter-op - Static methods

# Integer.valueOf("12")
." 12 ". ,< Integer/valueOf(String)/1 >, PRINTLN

# String.join(", ", "Hello", "world")
." world ". ." Hello ". ." ,  ". ,< String/join(CharSequence, CharSequence...)/3 >, PRINTLN 
