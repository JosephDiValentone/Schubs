

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 *  <i>SchubsH</i>. This class uses Huffman algorithm to compress a file or multiple files from the CLI
 *  The output must be a file location using relative file paths
 *  To run from the pom file:
 *  java -cp target/classes SchubsH <file location or glob>
 */

public class SchubsH 
{
	//Size of the ASCII
	private static final int R = 256; 
	public static boolean logging = true;
	static File input = null;
	static File output = null;
	static BinaryOut o = null;
	
	
	//Huffman trie node,, when in doubt, trie try again
	
	private static class Node implements Comparable <Node>{
		private final char ch;
		private final int freq;
		private final Node left,right;
		
		Node(char ch, int freq, Node left,Node right){
			this.ch = ch;
			this.freq =freq;
			this.left = left;
			this.right = right;
		}
		
		//is the node a leaf node?
		private boolean isLeaf() {
			assert (left == null && right == null) || (left != null && right != null);
			return(left == null && right == null);
		}
		
		public int compareTo(Node that) {
			return this.freq- that.freq;
		}
		
	}
	
	public static void err_print(String msg) {
//		if(logging)
//			System.err.print(msg);
	}
	
	public static void err_println(String msg) {
//		if (logging)
//			System.err.println(msg);
	}
	
	public static void compress(char[] input) throws IOException {
		
		//read the input,,, needs to be a file now
		//String s = BinaryStdIn.readString();// so this gives me the name of the file ex: wex.txt
	
		
		//tabulate the frequency counts
		int[] freq = new int[R];
		for (int i = 0; i< input.length; i++)
			freq[input[i]]++;
		
		//build Huffman trie
		Node root = buildTrie(freq);
		
		//build code table
		String [] st = new String[R];
		buildCode(st,root,"");
		
		//print trie for decoder
		writeTrie(root);
		err_println("writeTrie");
		
		//print number of bytes in original uncompressed message
        //System.out.println(input.length);
		//BinaryStdOut.write(input.length);// woulda thoght this was an int but its a char
		o.write(input.length);
		err_println("writing input length "+ input.length);
		
		err_println("maddly enconding....");
		
		//huffman code to encode the uncompressed input
		for (int i = 0; i<input.length;i++) {
			String code = st[input[i]];
			err_print("Char "+ input[i]+" ");
			
			for(int j = 0;j<code.length(); j++) {
				if (code.charAt(j)=='0') {
					o.write(false);
					err_print("0");
				}
				else if (code.charAt(j)=='1') {
					o.write(true);
					err_print("1");
				}
				else throw new RuntimeException("Illegal countrytime");
			}
			err_println("11");
		}
		
		// flush the output stream
		//BinaryStdOut.flush();
	}
	
	// build the huffman trie given the frequencies
	private static Node buildTrie (int[] freq) {
		
		//initialize priority queue eue with singleton trees
		MinPQ<Node>pq = new MinPQ<Node>();
		for(char i = 0; i<R;i++)
		{
			if (freq[i]>0)
				pq.insert(new Node(i,freq[i],null,null));
		}
			
			
			//merge two smallest trees 
			while (pq.size() > 1) {
				Node left = pq.delMin();
				Node right = pq.delMin();
				Node parent = new Node('\0', left.freq + right.freq,left,right);
				
				err_println("left.frq "+ left.freq + " right freq" + right.freq);
				pq.insert(parent);
			}
			return pq.delMin();
		}
		
		
		//wite compressed bitstring- encoded trie to standard output
		private static void writeTrie (Node x) throws FileNotFoundException {
			if(x.isLeaf()) {
				o.write(true);// boolean
				o.write(x.ch);// character at the node
				err_println("T "+ x.ch);
				return;
			}
			o.write(false);
			err_print("F");
			
			writeTrie(x.left);
			writeTrie(x.right);
		}
		
		//make a lookup table from symbols and their encodings
		private static void buildCode(String[] st, Node x, String s) {
			if (!x.isLeaf()) {
				buildCode(st,x.left,s+'0');
				buildCode(st,x.right, s+'1');
			}
			else {
				st[x.ch] = s;
				err_println("buildCode "+ x.ch +" "+s);
			}
		}
		
		
		//expand Huffman- encoded input from the standard input and write to standard output 
	
		
	private static  Node readTrie() throws FileNotFoundException {
		boolean isLeaf = BinaryStdIn.readBoolean();
		if (isLeaf) {
			char x = BinaryStdIn.readChar();
			err_println("t: "+x);
			return new Node(x,-1,null,null);
		}
		else {
			err_print("f");
			return new Node('\0', -1, readTrie(), readTrie());
		}
	}
	
	
	
    public static void main( String[] args ) throws IOException
    {
    	
    	for(int p = 0; p < args.length;p++) {
    		String i = args[p];
    		input = new File(i);
    		 i += ".hh";
    		// System.out.println("moke " + i);
    		 o = new BinaryOut(i);
    		 
    		 //System.out.println(input.exists());
    		output = new File(i);
    		String stringIn = "";
    		
    		if(input.exists())
    			stringIn =  new String(Files.readAllBytes(Paths.get(input.toString())));
    		
    		char[] inputc = stringIn.toCharArray();
    		if(!stringIn.isEmpty())
    		compress(inputc);
    		o.close();
    	}
    	
        //System.out.println( "Hello World!" );
    }
}
