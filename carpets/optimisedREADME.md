Optimised - Carpet Matching Algorithm

This Java program implements an algorithm to generate and analyze combinations of carpet strips for a given set of pieces. The program aims to find the most optimal combinations based on various criteria such as maximizing or minimizing the number of matches between adjacent strips, or finding a balanced combination of matches and non-matches.

Usage:
To run the program, compile both the carpetMain.java and carpetMethods.java files and execute the generated class file.

Compile:
javac carpetMethods.java
javac carpetMain.java


Run:
java carpetMain

Options:
The program accepts the following command-line options:
- -n <size>: Finds combinations with the least number of matches for a given size.
- -m <size>: Finds combinations with the most number of matches for a given size.
- -b <size>: Finds the best-balanced carpet for a given size.
- Default: Finds combinations with the least number of matches for a size determined by the input data.

Input:
The input data should be provided via standard input (stdin). Each line represents a strip of carpet, with 'R', 'G', and 'B' indicating colors.

Example Input:
RRR
BBR
BGG
GGG
...

Output:
The program outputs the selected carpet combinations along with relevant statistics, such as the number of matches or the balance score.

For detailed explanations of advanced options and functionalities, refer to the advanced functions section in the source code.

For any issues or inquiries, please contact the author.
