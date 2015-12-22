import java.nio.charset.StandardCharsets;

public class OatdataSection {
	public OATHeader header;
	public int headersize;
	
	//Oat Dex Header
	public BData dex_file_location_size;
	public BData dex_file_location_data;
	public BData dex_file_location_checksum;
	public BData dex_file_pointer;
	public BData classes_offsets;
	
	//Dex File
	public DEXFile dexfile;
	
	public OatdataSection (byte[] src, int off){
		int doff = off;
		header = new OATHeader(src, off);
		headersize = 84 + header.key_value_store.bSize;
		int hs = headersize;
		doff = off + hs;
		dex_file_location_size = new BData(doff + 0, new byte[]{src[doff + 0],
				src[doff + 1], src[doff + 2], src[doff + 3]});
		int lsize = Convertions.bytesToInt(dex_file_location_size.data, 0, dex_file_location_size.bSize);
		byte[] ldata = new byte[lsize];
		for (int i = 0; i < lsize; i++){
			ldata[i] = src[doff + 4 + i];
		}
		doff += (4 + lsize);
		dex_file_location_checksum = new BData(doff + 0, new byte[]{src[doff +0],
				src[doff +1], src[doff +2], src[doff + 3]});
		dex_file_pointer = new BData(doff + 4, new byte[]{src[doff +4],
				src[doff +5], src[doff +6], src[doff + 7]});
		
		dexfile = new DEXFile(src, off +
				Convertions.bytesToInt(dex_file_pointer.data, 0,dex_file_pointer.bSize));
		
		//class offset size extraction from header
		int cosize = Convertions.bytesToInt(dexfile.header.class_defs_size.data,
				0, dexfile.header.class_defs_size.bSize);
		byte [] codata = new byte[cosize];
		for (int i = 0; i < cosize; i++){
			codata[i] = src[doff + 8 + i];
		}
		classes_offsets = new BData(doff + 8, codata);
	}
}
