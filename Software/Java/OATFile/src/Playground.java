

public class Playground {

	public static void main(String[] args) {
	
		ELFFile oldf = new ELFFile("old.oat");
		
		FileOperations.writeBytesToFile("t.oat", oldf.getBytes());
		
		ELFFile nf = new ELFFile("t.oat");
		nf.dump();
	}
}
