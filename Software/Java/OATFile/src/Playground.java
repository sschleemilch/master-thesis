public class Playground {

	public static void main(String[] args) {
		ELFFile f = new ELFFile("C:\\Users\\basti\\Dropbox\\Studium\\"
				+ "Masterarbeit\\master-thesis\\Software\\Java\\OATFile\\"
				+ "src\\oatFile.oat");
		f.header.dump();	
		f.shtable.dump();
		f.sytable.dump();
	}
}
