import java.io.File;

// This is the start of lzw compression for 
//the final project of software engineering.
//My name is Joseph DiValentone and I am one of few students
//determined to pass this class in order to graduate

/**
 *  <i>SchubsL</i>. This class uses LZW to compress a file or multiple files from the CLI
 *  The output must be a file location using relative file paths
 *  To run from the pom file:
 *  java -cp target/classes SchubsL <file location or glob>
 */

public class SchubsL{
	private static final int R = 256;// input characters
	private static final int L = 4096;//codewords 2^W
	private static final int W = 12;// codeword width
	static String input = ""; 
	static BinaryIn bin1 = null;
	static BinaryOut o = null;
	
	public static void compress() {
		//input = BinaryStdIn.readString();
		TST<Integer> st = new TST<Integer>();
		//System.out.println(input);
		// all of the input characters
		for (int i = 0; i < R; i++) {
			
			st.put(""+(char) i, i);
		}
		int code = R+1; // codeword for EOF
		//System.out.println(input);
		while (input.length() > 0) {
			//System.out.println(input);
			String s = st.longestPrefixOf(input);// max prefix match
			o.write(st.get(s),W);// s's encoding
			int t = s.length();
			if (t< input.length()&& code<L) {// add s to symbol table
				st.put(input.substring(0,t+1), code++);
			}
			input = input.substring(t); // scan past s in input
		}
		o.write(R,W);
		//o.close();// alwyas remember to close the binarysS!!!!!
		
	}
	
	
	public static void main(String[] args) {
		

		// since we have to use path separators ittl look something like this
		// "src"+ File.separator+ "files"+ File.separator+ args[p]
		// where args[p] should be the actual file name with no separators
		try {
		for (int p = 0;  p< args.length; p++) {
			//String inputf = "src"+ File.separator+ "files"+ File.separatorChar+ args[p];
			
			bin1 = new BinaryIn(args[p]);
			//System.out.println("Boolean: "+ bin1.exists());
			//System.out.println("is empty" + bin1.isEmpty());
			if(bin1.exists()) {
			while (!bin1.isEmpty()) {
				input += bin1.readChar();
				}
			bin1.close();//CLOSE THE DOOR
			}
			
			// now afding the extension
			
			String oFile = args[p] +".ll";
			o = new BinaryOut(oFile);
			compress();
			o.close();
			
		}
		}finally {}
		
		
		
	}
	
	
	
	
	
	
	
	
}



