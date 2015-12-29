import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ELFOatdataSection extends Section{
	public OATHeader header;
	public int headersize;
	
	//Oat Dex Header
	public BData dex_file_location_size;
	public BData dex_file_location_data;
	public BData dex_file_location_checksum;
	public BData dex_file_pointer;
	public BData classes_offsets;
	
	public OATClassHeader[] oat_class_headers;
	
	public int cosize = -1;
	public int doff;

	private int offset;
	private int size;
	
	//Oat Class Header start and end
	public int chstart;
	public int chend;
	public byte[] chcontent;
	
	public byte[] bytes;

	
	//Dex File
	public DEXFile dexfile;
	
	public ELFOatdataSection (byte[] src, int off, int size){
		doff = off;
		header = new OATHeader(src, off);
		headersize = 84 + header.key_value_store.bSize;
		int hs = headersize;
		doff = off + hs;
		dex_file_location_size = new BData(doff + 0, new byte[]{src[doff + 0],
				src[doff + 1], src[doff + 2], src[doff + 3]});
		int lsize = dex_file_location_size.getInt();
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
		
		dexfile = new DEXFile(src, off + dex_file_pointer.getInt());
		
		//class offset size extraction from header
		cosize = dexfile.header.class_defs_size.getInt();
		byte [] codata = new byte[cosize*4];
		for (int i = 0; i < cosize*4; i++){
			codata[i] = src[doff + 8 + i];
		}
		classes_offsets = new BData(doff + 8, codata);
		
		oat_class_headers = new OATClassHeader[cosize];
		for (int i = 0; i < cosize; i++){
			int abs_off = off + Convertions.bytesToInt(Arrays.copyOfRange(classes_offsets.data, i*4, i*4+4),0,4);
			//compute size
			int sze = 0;
			if (i < cosize - 1){
				int abs_off_next = off + Convertions.bytesToInt(Arrays.copyOfRange(classes_offsets.data, (i+1)*4, (i+1)*4+4),0,4);
				sze = abs_off_next - abs_off;
			}	
			oat_class_headers[i] = new OATClassHeader(src, abs_off, sze);
		}
		
		chstart = oat_class_headers[0].getOffset();
		chend = getZerosOffset(src, chstart);
		
		//fill last entry
		int abs_off = off + Convertions.bytesToInt(Arrays.copyOfRange(classes_offsets.data,
				(cosize-1)*4, (cosize-1)*4+4),0,4);
		oat_class_headers[cosize-1] = new OATClassHeader(src, abs_off, chend-abs_off-0x72);
		
		chcontent = Arrays.copyOfRange(src, chstart, chend);
		offset = off;
		this.size = size;
		this.bytes = Arrays.copyOfRange(src, off, off + size);
	}
	
	public int getZerosOffset(byte[]src, int chstart){
		int zerocount = 0;
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
				dex_file_location_size.getInt());
		System.out.println("|--------Dex Path:\t\t" + new String(dex_file_location_data.data,
				StandardCharsets.UTF_8));
		System.out.print("|--------Dex Checksum:\t\t");
		System.out.printf("0x%08X\n", dex_file_location_checksum.getInt());
		System.out.print("|--------Dex Offset(oatdata):\t");
		System.out.printf("0x%08X\n", dex_file_pointer.getInt());
		System.out.println("|--------OatClassHeader Offsets (oatdata):\t");
		for (int i = 0; i < cosize*4; i+=4){
			System.out.print("|------------");
			int off = Convertions.bytesToInt(Arrays.copyOfRange(classes_offsets.data,
					i, i+4), 0, 4);
			System.out.printf("0x%08X\n", off);
		}
		System.out.println("|----Oat Dex File Header");
		
		System.out.println("|----Oat Class Headers");
		System.out.print("|--------Offset:\t\t");
		System.out.printf("0x%08X\n", chstart);
		System.out.print("|--------Size:\t\t\t");
		System.out.printf("0x%08X\n", chend-chstart);
		for (int i = 0; i < oat_class_headers.length; i ++){
			oat_class_headers[i].dump();
		}
		System.out.println("|----Oat Class Headers");
		dexfile.dump();
		System.out.println("|--ELF Oatdata Section");
	}

	
	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

	public void setSize(int size){
		this.size = size;
	}
	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public void setOffset(int off) {
		offset = off;	
	}
}
