###################################################################################################
### Takes whatever is on the stack and compiles LIT <foo>
### ( a - )
: LITERAL IMMEDIATE
  ' LIT ,           # compile LIT
  ,                 # compile the literal itself (from the stack)
;

###################################################################################################
: \n
 ." 
 ".
;

###################################################################################################
: CR \n EMIT ;

###################################################################################################
: BL ."   ". ;

###################################################################################################
: SPACE BL EMIT ;
 
###################################################################################################
### ( a - b )
: NEGATE 0 SWAP - ;

###################################################################################################
: TRUE  1 ;
: FALSE 0 ;
: NOT   0= ;

###################################################################################################
: [COMPILE] IMMEDIATE
  WORD
  FIND
  >CFA
  ,
;

###################################################################################################
: IF IMMEDIATE
  ' 0BRANCH ,  
  HERE        
  0 , 
;

###################################################################################################
: THEN IMMEDIATE
  DUP
  HERE -
  SWAP !
;

###################################################################################################
: ELSE IMMEDIATE
  ' BRANCH ,
  HERE
  0 ,
  SWAP
  DUP
  HERE -
  SWAP !
;

###################################################################################################
: UNLESS IMMEDIATE
  ' NOT ,
  [COMPILE] IF
;

###################################################################################################
: BEGIN IMMEDIATE
  HERE
;

###################################################################################################
: UNTIL IMMEDIATE
  ' 0BRANCH ,
  HERE SWAP -
  ,
;

###################################################################################################
: AGAIN IMMEDIATE
  ' BRANCH ,
  HERE SWAP -
  ,
;

###################################################################################################
: WHILE IMMEDIATE
  ' 0BRANCH ,
  HERE
  0 ,
;

###################################################################################################
: REPEAT IMMEDIATE
  ' BRANCH ,
  SWAP
  HERE SWAP - ,
  DUP
  HERE SWAP -
  SWAP !
;

###################################################################################################
### ( x y -- y ) 
: NIP 
  SWAP DROP 
;

###################################################################################################
### ( x y -- y x y )
: TUCK  
  SWAP OVER 
;

###################################################################################################
### ( x_u ... x_1 x_0 u -- x_u ... x_1 x_0 x_u )
: PICK 
  1+
  DSP@@
;
###################################################################################################

: ( IMMEDIATE
  1	
  BEGIN
    KEY
    DUP ." ( ". = IF
      DROP
      1+
    ELSE
      ." ) ". = IF
        1-
      THEN
    THEN
  DUP 0= UNTIL
  DROP
;

###################################################################################################

: /MOD ( x y - quotient remainder )
  2DUP
  SWAP
  /
  ROT ROT
  SWAP
  MOD 
;

###################################################################################################

: SPACES ( n -- )
  BEGIN
    DUP 0>
  WHILE
    SPACE
    1-
  REPEAT
  DROP
;

###################################################################################################

: DECIMAL ( -- ) 
  10 BASE ! 
;

: HEX ( -- ) 
  16 BASE ! 
;

###################################################################################################

: U. ( x -- )
  ,< String/valueOf(Integer)/1 >,
  PRINT
;

###################################################################################################

: U.R # ( x width -- )
  DUP
  0<> IF
    ,< String/valueOf(Integer)/1 >, 
     ." d ". 
     ." %0 ". 
     ROT
     ,< String/join(CharSequence, CharSequence...)/3 >,   
     .< formatted(Object...)/1 >.
  ELSE
    ,< String/valueOf(Integer)/1 >, 
  THEN   
  PRINT
;

###################################################################################################

: . 
  0 
  U.R SPACE 
;
