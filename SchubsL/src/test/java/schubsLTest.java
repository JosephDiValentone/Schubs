import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//import main.java.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

/**
 *  <i>SchubsLTest</i>. This class uses schubsL and Deschubs to compress a file or multiple files from the CLI
 * 	it will then be decompressed using Deschubs in order to assert and compare
 *  The class creates 4 random files and populates them with random strings
 *  To run from the pom file:
 *  mvn test
 */
public class schubsLTest{
	final String location = "src"+File.separator+"files"+File.separator;
	String ALLthethings = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz"
            +"!"; 
	
	public String CompressTest(String[] args) throws IOException {
		
		String Result = "";// emptystring to return
		// iterates through the array and grabs all the arguments
		for (String i : args) {
			// not going to be using blee.tars
			
			BinaryIn unbin1 = new BinaryIn(i);// open the file stream
		
			while(!unbin1.isEmpty()) {
				//System.out.println(Result + "here the de");
				Result += unbin1.readChar();
				}// Concats the file contents to the string
			// close the stream
			//System.out.println(unbin1.exists());
			if(unbin1.exists())
				unbin1.close();
			//call a file variable for the file and delete the file.
		
			//assert(!bl.exists());// prove the file is deleted
			
		}
		return Result;
		
		
		
	}
	
	
	String randomString(int x)  throws IOException {		
		// makes random string builder to make the thing
		StringBuilder b = new StringBuilder(x);
		
		for(int i = 0; i<x; i++ ) {
			int addto = (int)(ALLthethings.length()*Math.random());
			// grab a random int from the list and add to the builder
			b.append(ALLthethings.charAt(addto));	
		}
		return b.toString();	
	}
	
	// return the String[] with names of the file locations to run throug main
	public String[] createFiles()  throws IOException{
		// create 5 random files and write random strings to the
		String[] Results = {"","","",""};
		for(int i = 0; i< 4; i++) {
			String TF = location+ randomString(10)+ ".txt"; // file name location
			Results[i] = TF;// String to return at the end
			FileOutputStream out = new FileOutputStream(TF);// open the stream to write to it
			int populate =1+ (int) (Math.random()*21);
			if (populate == 0)//base casehandled elsewhere
			{populate += (i+2)*3;}
			//System.out.println(populate + " " + Results[i]);
			out.write(randomString(populate).getBytes());// populate the file with random data
			out.close();//CLOSE THE DOOR
		}
		return Results;
		
	}
	
	
	@Test
	public void random4 () throws IOException {
		String[] args= createFiles();// create 4 main files
		//System.out.println(args.length);
		schubsL.main(args);//call the compression
		//System.out.println("make me a pie" );
		String before = CompressTest(args);
		// collect the uncompressed version and delete the un compressed files
		String[] UCList = {"","","",""};// creatre the list to be used for uncompression
		// the compression algorithm should have added the .hh at the end
		//System.out.println("make me a pie" );
		for ( int q = 0 ; q < args.length; q++) {
			UCList[q] = args[q] +".ll"; // adding the extension
		}
		Deschubs.main(UCList);// uncompresses the files and re create the .txt files
//		for(int z = 0; z< UCList.length;z++) {// delete the compressed files
//			File bl = new File(UCList[z]);
//			Files.delete(bl.toPath());
//			assert(!bl.exists());
//		}
		//System.out.println(args.length);
		String after = CompressTest(args);// collect the uncompressed version and delete the files
	//	System.out.println(after );
	//	System.out.println(before);
		assert(after.equals(before)); 
		
	}
	
	
	
	
	 //one empty file and one file that does not exist and is not created in the test folder
	@Test
	public void BaseCases() throws IOException{
		String MTFile = location + "emptyDeal.txt"; // empty file
		FileOutputStream outt = new FileOutputStream(MTFile);// creates a file withput population
		outt.close();//CLOSE THE DOOR
		String[] args = {MTFile};// call the main
		schubsL.main(args);
		String before = CompressTest(args);
		
		String[] UCList = {"",""};// creatre the list to be used for uncompression
		// the compression algorithm should have added the .hh at the end
		//System.out.println("make me a pie" );
		for ( int q = 0 ; q < args.length; q++) {
			UCList[q] = args[q] +".ll"; // adding the extension
		}
		FileOutputStream outtt = new FileOutputStream(UCList[0]);// creates a file withput population
		outtt.close();
		Deschubs.main(UCList);// uncompresses the files and re create the .txt files
		String after = CompressTest(args);// collect the uncompressed version and delete the files
		File bl = new File(UCList[0]);
		Files.delete(bl.toPath());
		assert(!bl.exists());
		
		
		assert(after.equals(before));

	}
	
	
	
}