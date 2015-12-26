import java.util.Arrays;

//DEX File not completely specified so far...
public class DEXFile extends ELFSection{
	public DEXHeader header;
	public DEXMapList maplist;
	
	private int size;
	private int offset;
	byte[] bytes;
	
	public DEXFile(byte[] src, int off){
		header = new DEXHeader(src, off);
		maplist = new DEXMapList(src, off + Convertions.bytesToInt(header.map_off.data, 0, header.map_off.bSize));
		offset = off;
		size = Convertions.bytesToInt(header.file_size.data, 0, header.file_size.bSize);
		bytes = Arrays.copyOfRange(src, off, off+size);
	}
	
	public void dump(){
		System.out.println("\nDEX FILE ------------------------------------>");
		header.dump();
		maplist.dump();
		System.out.println("\nEND OF DEX FILE -----------------------------<");
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getOffset() {
		return offset;
	}
}
