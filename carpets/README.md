Carpet Optimization Tool

By Alexander Counsell 1810077, Anthony Dong 2169260, Anthony Deng 8264774, Jayden Tohill 8551796

Description

This Java program is designed to take a bunch of strips of carpet as input and optimize the placement of the strips depending on the mode.

The modes are as follows...

    -m option finds the combination of carpet with the maximum number of matches.
    -n option finds the combination of carpet with the minimum number of matches.
    -b option finds the combination of carpet with a balanced number of matches. This means there will be as many matches are non-matches. Some carpets cannot      accomodate  a perfect number of matches as non-matches, but this option will get as close as possible


How to Compile and Run

    Compile the Java program using 
        - javac carpetMethods.java
        - javac carpetMain.java
    Run the program using carpetMain, followed by an option:
        -n [size]: Specify the size of the carpet to compute the minimum matches.
        -m [size]: Specify the size of the carpet to compute the maximum matches.
        -b [size]: Specify the size of the carpet and perform multiple iterations to find the most balanced arrangement.

Input Format

    The program reads strings from the standard input (stdin). Each string represents a carpet strip, and should be entered one per line.


No external libraries are used.

