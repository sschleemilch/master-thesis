
public class DEXFile {
	public DEXHeader header;
	public DEXMapList maplist;
	
	public DEXFile(byte[] src, int off){
		header = new DEXHeader(src, off);
		maplist = new DEXMapList(src, off + Convertions.bytesToInt(header.map_off.data, 0, header.map_off.bSize));
	}
	
	public void dump(){
		System.out.println("\nDEX FILE ------------------------------------>");
		header.dump();
		maplist.dump();
		System.out.println("\nEND OF DEX FILE -----------------------------<");
	}
}
