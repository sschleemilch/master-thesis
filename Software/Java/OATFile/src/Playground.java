

public class Playground {

	public static void main(String[] args) {
		ELFFile f = new ELFFile("old.oat");
		f.oatdata.dexfile.dump();
	}
}
