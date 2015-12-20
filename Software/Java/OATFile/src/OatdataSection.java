
public class OatdataSection {
	public OATHeader header;
	public int headersize;
	
	
	//Oat Dex Header...
	public BData dex_file_location_size;
	public BData dex_file_location_data;
	public BData dex_file_location_checksum;
	public BData dex_file_pointer;
	public BData classes_offsets;
	
	public OatdataSection (byte[] src, int off){
		header = new OATHeader(src, off);
		headersize = 84 + header.key_value_store.bSize;
		int hs = headersize;
		
		//Oat Dex Header...
		off = off + hs;
		dex_file_location_size = new BData(off+0, new byte[]{src[off+0],
				src[off+1],src[off+2], src[off+3]});
		int psize = Convertions.bytesToInt(dex_file_location_size.data, 0, dex_file_location_size.bSize);
		byte [] pathbytes = new byte[psize];
		for (int i = 0; i < psize; i++){
			pathbytes[i] = src[off + 4 + i];
		}
		dex_file_location_data = new BData(off+4, pathbytes);
		off = off+psize;
		dex_file_location_checksum = new BData(off + 0, new byte[]{src[off+0],
				src[off+1],src[off+2], src[off+3]});
		dex_file_pointer = new BData(off + 4, new byte[]{src[off+4],
				src[off+5],src[off+6], src[off+7]});
		classes_offsets = null; //cant be set before Dex File 
		
	}
}
