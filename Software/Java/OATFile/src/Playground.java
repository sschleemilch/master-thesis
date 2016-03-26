

public class Playground {

	public static void main(String[] args) {
		ELFFile oldf = new ELFFile("src/oatFile.oat");
		
		//oldf.oatdata.dexfile.deleteDEXInsns();
		//oldf.oatdata.updateDEXFileContent();
		//oldf.oatdata.dexfile.dump();
		//FileOperations.writeBytesToFile("heiseinsdel.oat", oldf.getBytes());
		oldf.dump();
	}
}
