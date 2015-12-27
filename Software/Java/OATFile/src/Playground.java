

public class Playground {

	public static void main(String[] args) {
		ELFFile f1 = new ELFFile("C:\\Users\\basti\\Dropbox\\Studium\\"
				+ "Masterarbeit\\master-thesis\\Software\\Java\\OATFile\\"
				+ "src\\oatFile.oat");
		ELFFile f2 = new ELFFile("C:\\Users\\basti\\Dropbox\\Studium\\"
				+ "Masterarbeit\\master-thesis\\Software\\Java\\OATFile\\"
				+ "src\\oatFile.oat");
		
		f1.injectExecutable(f2.getExecutable());
		f1.dump();
		FileOperations.writeBytesToFile("C:\\Users\\basti\\Dropbox\\Studium\\"
				+ "Masterarbeit\\master-thesis\\Software\\Java\\OATFile\\"
				+ "src\\test.oat", f1.getBytes());
	}
}
