#!/usr/bin/env perl

use strict ;
use warnings ;
use diagnostics ;
use utf8 ;
use feature ':5.38' ;
use experimental qw(try) ;

###################################################################################################

my $jarFile = shift or die("Path to JAR file not provided.");
my $expectedOutputFile = shift or die("Path to expected output file not provided.");
my $testCodeFile = shift or die("Path to test code not provided.");
my $libraryFile = shift or die("Path to bjForth.forth not provided.");

open(my $fh, '<', $expectedOutputFile) or die("open(): $!");
my $expectedOutput = do { local($/); <$fh> };
close($fh);

### End of stream should mean a graceful exit

my $actualOutput = qx/cat ${libraryFile} ${testCodeFile} | java -jar ${jarFile} 2>&1/;

if ($actualOutput eq $expectedOutput) {
  exit 0;
} else {
  die("E2E tests failed.\nActual output is\n\n${actualOutput}");
}
