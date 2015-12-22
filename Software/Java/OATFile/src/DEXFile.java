
public class DEXFile {
	public DEXHeader header;
	
	public DEXFile(byte[] src, int off){
		header = new DEXHeader(src, off);
	}
	
	public void dump(){
		System.out.println("DEX FILE ------------------------------------>");
		header.dump();
		System.out.println("END OF DEX FILE -----------------------------<");
	}
}
