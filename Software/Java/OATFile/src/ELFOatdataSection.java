import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ELFOatdataSection extends ELFSection{
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
	

	private int offset;
	private int size;
	
	//Oat Class Header start and end
	public int chstart;
	public int chend;
	public byte[] chcontent;

	
	//Dex File
	public DEXFile dexfile;
	
	public ELFOatdataSection (byte[] src, int off, int size){
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
		
		chstart = oat_class_headers[0].getOffset();
		chend = getZerosOffset(src, chstart);
		chcontent = Arrays.copyOfRange(src, chstart, chend);
		offset = off;
		this.size = size;
	}
	
	private int getZerosOffset(byte[]src, int chstart){
		int zerocount = 0;
		boolean followingZero = false;
		int sp = chstart;
		int zerostart = 0;
		while(zerocount < 20){
			if(src[sp++] == 0){
				if(zerocount == 0){
					zerostart = sp-1;
				}
				zerocount++;
			}else{
				zerocount = 0;
			}
		}
		return zerostart;
	}
	
	public void dump(){
		System.out.println("|");
		System.out.println("|--ELF Oatdata Section");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		header.dump();
		System.out.println("|----Oat Dex File Header");
		System.out.println("|--------Dex Path Length:\t" + 
				Convertions.bytesToInt(dex_file_location_size.data, 0, dex_file_location_size.bSize));
		System.out.println("|--------Dex Path:\t\t" + new String(dex_file_location_data.data,
				StandardCharsets.UTF_8));
		System.out.print("|--------Dex Offset(oatdata):\t");
		System.out.printf("0x%08X\n", Convertions.bytesToInt(dex_file_pointer.data, 0, dex_file_pointer.bSize));
		System.out.println("|--------OatClassHeader Offsets (oatdata):\t");
		for (int i = 0; i < cosize*4; i+=4){
			System.out.print("|------------");
			int off = Convertions.bytesToInt(Arrays.copyOfRange(classes_offsets.data,
					i, i+4), 0, 4);
			System.out.printf("0x%08X\n", off);
		}
		System.out.println("|----Oat Dex File Header");
		
		System.out.println("|----Oat Class Headers");
		for (int i = 0; i < oat_class_headers.length; i ++){
			oat_class_headers[i].dump();
		}
		System.out.println("|----Oat Class Headers");
		dexfile.dump();
		System.out.println("|--ELF Oatdata Section");
	}

	@Override
	public byte[] getBytes() {
		byte[] bytes = new byte[size];
		
		//fill with header
		byte[] tmp = header.getBytes();
		int bp = 0;
		for (int i = 0; i < tmp.length; i++){
			bytes[bp++] = tmp[i];
		}
		//fill in oat dex file header
		BData[] bd = {dex_file_location_size, dex_file_location_data,
				dex_file_location_checksum, dex_file_pointer, classes_offsets};
		for (int i = 0; i < bd.length; i++){
			for(int j = 0; j < bd[i].bSize; j++){
				bytes[bp++] = bd[i].data[j];
			}
		}
		//fill in oat class headers 
		for(int i = 0; i < chcontent.length; i++){
			bytes[bp++] = chcontent[i];
		}
		// fill in filling zeros
		for(int i = bp; i < bytes.length; i++){
			bytes[i] = 0x00;
		}
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
