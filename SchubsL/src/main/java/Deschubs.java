
// Schuby dooby do 
// this expands the ll or hh
/**
 *  <i>Deschubs</i>. This class uses Deschubs to decompress the files given to it
 *  This class takes the ll or hh extension and  returns it to the original form
 *  Files must be given in the relative path
 *  To run from the pom file:
 *  java -cp target/classes Deschubs <File path or Glob>
 */

import java.io.File;
import java.io.FileNotFoundException;

import javafx.scene.Node;

public class Deschubs{
	private static final int R =256;
	private static final int L = 4096;
	private static final int W = 12;
	static BinaryIn bin1 = null;
	static String input = "";
	static BinaryOut o = null;
	static BinaryOut o2 = null;
	
	//Huffman Trie node
	private static class Node implements Comparable<Node>{
		private final char ch;
		private final int freq;
		private final Node left,right;
		
		Node (char ch, int freq, Node left, Node right){
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		
		//is the node a leaf node
		private boolean isLeaf() {
			assert(left == null && right == null) || (left != null && right != null);
			return (left == null && right == null);
		}
		
		//compare based on frequency
		public int compareTo(Node that) {
			return this.freq - that.freq;
		}	
	}
	
	
	
	
public static void expandL() {
	String[] st = new String[L];
	int i; // next available codeword value 
	
	//initialize symbol table with all 1- character strings
	for (i = 0; i <R; i++){
		st[i] = "" + (char) i;
	}
	st[i++] = "";
	
	int codeword = bin1.readInt(W);
	String val = st[codeword];
	
	while(true) {
		o.write(val);
		codeword = bin1.readInt(W);
		if(codeword == R) break;
		String s = st[codeword];
		if(i==codeword) s = val + val.charAt(0);
		if (i<L) st[i++] = val + s.charAt(0);
		val = s;
	}
	o.close();
}

//expand Huffman-encoded input from input

public static void expandH() throws FileNotFoundException{
	
	//read in tree from input stream
	Node root = readTrie();
	
	//number of bytes to  write
	int length = bin1.readInt();
	//System.out.println(length);	
	//decode using huffman trie
	for(int i = 0; i <length; i++) {
		Node x  = root;
		while (!x.isLeaf()) {
			boolean bit = bin1.readBoolean();
			if (bit)
			{x = x.right;}
			else 	
			{x = x.left;}
		}
		//System.out.println(length);
		o2.write(x.ch);
	}
	o2.flush();
}

private static Node readTrie() {
	boolean isLeaf = bin1.readBoolean();
	if (isLeaf) {
		char x = bin1.readChar();
		//System.out.println("t: " + x );
		return new Node(x,-1,null,null);
	}
	else {
		//System.out.println("f");
		return new Node('\0', -1 , readTrie(),readTrie());
	}
	
}






public static void main(String[] args) throws FileNotFoundException{
	
	// since we have to use path separators ittl look something like this
	// "src"+ File.separator+ "files"+ File.separator+ args[p]
	// where args[p] should be the actual file name with no separators
	// args here should be the thing : blee.txt.hh
	for (int p = 0; p < args.length; p++) {
	 if (args[p].endsWith(".ll"))
	 {

		// remove the extension 
		String oFile =  args[p].substring(0, args[p].length() - 2); // blee.txt
		bin1 = new BinaryIn(args[p]);
		o = new BinaryOut(oFile);
		//System.out.println("Boolean: "+ bin1.isEmpty());
		if(!bin1.isEmpty()) {
		 expandL();
		}
		
		bin1.close();
		o.close();
	 }
	 else if (args[p].endsWith(".hh"))
	 {
		 
	//	String i ="src"+ File.separator+ "files"+ File.separator+ args[p];
		String oFile2 =  args[p].substring(0, args[p].length() - 2);
		//String outp2 ="src"+ File.separator+ "files"+ File.separator+ oFile2;
		
		bin1 = new BinaryIn(args[p]);
		o2 = new BinaryOut(oFile2); 
		if(!bin1.isEmpty())
		{expandH();}
		bin1.close();
		o2.close();
	 }
	 else
	 {//System.out.println("error, please use a proper file extension like a real human "+ args[0]);}
	}
	
	
}


}
}