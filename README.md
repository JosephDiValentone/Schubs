# Schubs
design: algorithm theory and trade offs
tests: prove and illustrate everything
installation: CLI instructions
test instructions
run examples: CLI instructions

Design: algorithm thoery and trade offs
Huffman:
Huffman algorithm uses a tree to utilize the frequency of the the inside of the file. Sadly, this means that the code for the compression can sometimes be bigger than the original file in thtaxase of smaller files. This means that it looses efficiancy the smaller a file gets.
LZW:
LZW algorithm uses a different type of algoritm that is dictionary based. It builds a dictionary and gains efficiancy based off of similar phrases that commonly pass thorugh the stream. This loses effiency the more random the file content is but increaces the more common and the more data is given to it


Installation and test instructions :
git clone master
cd <file>
(Possibly mvn compile)
mvn test

To Run: 
git clone master
cd <file>
 java -cp target/classes <file name ie. SchubsH> <file locations or Glob>
 
