

public class Playground {

	public static void main(String[] args) {
		ELFFile fMine = new ELFFile("C:\\Users\\basti\\Dropbox\\Studium\\"
				+ "Masterarbeit\\master-thesis\\Software\\Java\\OATFile\\"
				+ "src\\oatFile.oat");
		ELFFile fHeise = new ELFFile("C:\\Users\\basti\\Dropbox\\Studium\\"
				+ "Masterarbeit\\master-thesis\\Software\\Java\\OATFile\\"
				+ "src\\oatHeise.oat");
		
		fMine.injectExecutable(fHeise.getBytes());
		fMine.dump();
		FileOperations.writeBytesToFile("C:\\Users\\basti\\Dropbox\\Studium\\"
				+ "Masterarbeit\\master-thesis\\Software\\Java\\OATFile\\"
				+ "src\\test.oat", fMine.getBytes());
	}
}
