#!/usr/bin/env perl
# -*- mode: cperl; cperl-indent-level: 2; -*-
use strict ;
use warnings ;
use diagnostics ;
use utf8 ;
use feature ':5.38' ;
use Encode         qw(decode_utf8) ;
use File::Basename qw(fileparse) ;
use constant {
  true  => 0,
  false => 1
} ;

####################################################################################################

my $libraryFile = shift or die("Path to library not provided.");
my $outputFile = shift or die("Path to the output file not provided.");

####################################################################################################

open(my $fh, "/home/bahman/workspace/forth/bjforth/bjforth/src/main/forth/bjForth.forth" ) or die("open(): $!\n");

my $result = "";
my $toc = "# Words defined in the `bjForth.forth` library\n";
$toc .= "_Automatically generated from the source._\n\n";
$toc .= "### Table of contents\n";
my $in_docs = false;
my $in_docs_first_line = true;
while (my $line = <$fh>) {
  chomp($line);
  if ($line =~ /^#{99}$/) {
    if ($in_docs == true) {
      $in_docs = false;
      $result .= "\n"
    } else {
      $in_docs = true;
      $in_docs_first_line = true;
    }
  } elsif ($in_docs == true) {
    if ($line =~ /^#/) {
      $line =~ s/^# ?//;
      if ($in_docs_first_line == true) {
        $result .= "---\n";
        $result .= "\n" . "#### " . "`$line`" . "\n";
        $in_docs_first_line = false;
        $toc .= "\n  * `$line`";
      } else {
        $result .= $line . "\n";
      }
  }
  } else {
    # Ignore
  }
}

close($fh);

open ($fh, '>', "/home/bahman/workspace/forth/bjforth/docs/bjForth.forth.md") or die("open(): $!\n");

$result = $toc . $result;
say($fh $result);

close($fh);
