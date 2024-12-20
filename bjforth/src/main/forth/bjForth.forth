# Takes whatever is on the stack and compiles LIT <foo>
# ( a - )
: LITERAL IMMEDIATE
  ' LIT ,           # compile LIT
  ,                 # compile the literal itself (from the stack)
;

### Constant - Carriage return
: \n
 ." 
 ". LITERAL
;

### Prints a carriage return
: CR \n EMIT ;

### Constant - Blank
: BL ."  ". LITERAL ;

### Prints a SPACE character
: SPACE BL EMIT ;
 
###################################################################################################

# Leaves the negative of a number on the stack.
# ( a - b )
: NEGATE 0 SWAP - ;

# Standard words for booleans.
: TRUE  1 ;
: FALSE 0 ;
: NOT   0= ;

###################################################################################################

