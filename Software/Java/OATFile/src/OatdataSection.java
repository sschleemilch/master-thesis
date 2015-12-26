import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class OatdataSection extends ELFSection{
	public OATHeader header;
	public int headersize;
	
	//Oat Dex Header
	public BData dex_file_location_size;
	public BData dex_file_location_data;
	public BData dex_file_location_checksum;
	public BData dex_file_pointer;
	public BData classes_offsets;
	
	public OATClassHeader[] oat_class_headers;
	
	private int cosize = -1;
	
	public int size;
	public int offset;
	
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
		dex_file_location_data = new BData(doff + 4, ldata);
		doff += (4 + lsize);
		dex_file_location_checksum = new BData(doff + 0, new byte[]{src[doff +0],
				src[doff +1], src[doff +2], src[doff + 3]});
		dex_file_pointer = new BData(doff + 4, new byte[]{src[doff +4],
				src[doff +5], src[doff +6], src[doff + 7]});
		
		dexfile = new DEXFile(src, off +
				Convertions.bytesToInt(dex_file_pointer.data, 0,dex_file_pointer.bSize));
		
		//class offset size extraction from header
		cosize = Convertions.bytesToInt(dexfile.header.class_defs_size.data,
				0, dexfile.header.class_defs_size.bSize);
		byte [] codata = new byte[cosize*4];
		for (int i = 0; i < cosize*4; i++){
			codata[i] = src[doff + 8 + i];
		}
		classes_offsets = new BData(doff + 8, codata);
		
		oat_class_headers = new OATClassHeader[cosize];
		for (int i = 0; i < cosize; i++){
			int abs_off = off + Convertions.bytesToInt(Arrays.copyOfRange(classes_offsets.data, i*4, i*4+4),0,4);
			oat_class_headers[i] = new OATClassHeader(src, abs_off);
		}
		
		
		offset = off;
	}
	
	public void dump(){
		System.out.println("OATDATA SECTION ----------------------------->");
		header.dump();
		System.out.println("\nOAT DEX FILE HEADER -------------->");
		System.out.println("Dex Path Length:\t" + 
				Convertions.bytesToInt(dex_file_location_size.data, 0, dex_file_location_size.bSize));
		System.out.println("Dex Path:\t\t" + new String(dex_file_location_data.data,
				StandardCharsets.UTF_8));
		System.out.print("Dex Offset(oatdata):\t");
		System.out.printf("0x%08X\n", Convertions.bytesToInt(dex_file_pointer.data, 0, dex_file_pointer.bSize));
		System.out.println("OatClassHeader Offsets (oatdata):\t");
		for (int i = 0; i < cosize*4; i+=4){
			System.out.print("\t\t\t");
			int off = Convertions.bytesToInt(Arrays.copyOfRange(classes_offsets.data,
					i, i+4), 0, 4);
			System.out.printf("0x%08X\n", off);
		}
		System.out.println("\nEND OF OAT DEX FILE HEADER -------<");
		System.out.println("\nOAT CLASS HEADERS----------------->");
		for (int i = 0; i < oat_class_headers.length; i ++){
			oat_class_headers[i].dump();
		}
		System.out.println("\nEND OF OAT CLASS HEADERS----------<");
		dexfile.dump();
		System.out.println("END OF OATDATA SECTION ----------------------<");
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOffset() {
		return offset;
	}
}
