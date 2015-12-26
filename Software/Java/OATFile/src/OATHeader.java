import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class OATHeader extends ELFSection{
	
	public BData magic;
	public BData version;
	public BData adler32_checksum;
	public BData instruction_set;
	public BData instruction_set_features;
	public BData dex_file_count;
	public BData executable_offset;
	public BData interpreter_to_interpreter_bridge_offset;
	public BData interpreter_to_compiled_code_bridge_offset;
	public BData jni_dlsym_lookup_offset;
	public BData portable_imt_conflict_trampoline_offset;
	public BData portable_resolution_trampoline_offset;
	public BData portable_to_interpreter_bridge_offset;
	public BData quick_generic_jni_trampoline_offset;
	public BData quick_imt_conflict_trampoline_offset;
	public BData quick_resolution_trampoline_offset;
	public BData quick_to_interpreter_bridge_offset;
	public BData image_patch_delta;
	public BData image_file_location_oat_checksum;
	public BData image_file_location_oat_data_begin;
	public BData key_value_store_size;
	public BData key_value_store;
	
	private int size;
	private int offset;
	
	public String creationArguments;
	public String[] key_value_store_strings; 
	
	public OATHeader(byte[] src, int off){
		magic = new BData(off + 0, new byte[]{src[off+0],src[off+1],
				src[off+2],src[off+3]});
		version = new BData(off + 4, new byte[]{src[off+4],src[off+5],
				src[off+6],src[off+7]});
		adler32_checksum = new BData(off + 8, new byte[]{src[off+8],src[off+9],
				src[off+10],src[off+11]});
		instruction_set = new BData(off + 12, new byte[]{src[off+12],src[off+13],
				src[off+14],src[off+15]});
		instruction_set_features = new BData(off + 16, new byte[]{src[off+16],src[off+17],
				src[off+18],src[off+19]});
		dex_file_count = new BData(off + 20, new byte[]{src[off+20],src[off+21],
				src[off+22],src[off+23]});
		executable_offset = new BData(off + 24, new byte[]{src[off+24],src[off+25],
				src[off+26],src[off+27]});
		interpreter_to_interpreter_bridge_offset = new BData(off + 28, new byte[]{src[off+28],
				src[off+29], src[off+30],src[off+31]});
		interpreter_to_compiled_code_bridge_offset = new BData(off + 32, new byte[]{src[off+32],
				src[off+33],src[off+34],src[off+35]});
		jni_dlsym_lookup_offset = new BData(off + 36, new byte[]{src[off+36],src[off+37],
				src[off+38],src[off+39]});
		portable_imt_conflict_trampoline_offset = new BData(off + 40, new byte[]{src[off+40],
				src[off+41],src[off+42],src[off+43]});
		portable_resolution_trampoline_offset = new BData(off + 44, new byte[]{src[off+44],src[off+45],
				src[off+46],src[off+47]});
		portable_to_interpreter_bridge_offset = new BData(off + 48, new byte[]{src[off+48],src[off+49],
				src[off+50],src[off+51]});
		quick_generic_jni_trampoline_offset = new BData(off + 52, new byte[]{src[off+52],src[off+53],
				src[off+54],src[off+55]});
		quick_imt_conflict_trampoline_offset = new BData(off + 56, new byte[]{src[off+56],src[off+57],
				src[off+58],src[off+59]});
		quick_resolution_trampoline_offset = new BData(off + 60, new byte[]{src[off+61],src[off+62],
				src[off+63],src[off+64]});
		quick_to_interpreter_bridge_offset = new BData(off + 64, new byte[]{src[off+64],src[off+65],
				src[off+66],src[off+67]});
		image_patch_delta = new BData(off + 68, new byte[]{src[off+68],src[off+69],
				src[off+70],src[off+71]});
		image_file_location_oat_checksum = new BData(off + 72, new byte[]{src[off+72],src[off+73],
				src[off+74],src[off+75]});
		image_file_location_oat_data_begin = new BData(off + 76, new byte[]{src[off+76],src[off+77],
				src[off+78],src[off+79]});
		key_value_store_size = new BData(off + 80, new byte[]{src[off+80],src[off+81],
				src[off+82],src[off+83]});
		
		int kssize = Convertions.bytesToInt(key_value_store_size.data, 0, key_value_store_size.bSize);
		byte[] ksbytes = new byte[kssize];
		for (int i = 0; i < kssize; i++){
			ksbytes[i] = src[off+84+i];
		}
		key_value_store = new BData(off+80, ksbytes);
		key_value_store_strings = extractKeyPairStrings();
		creationArguments = key_value_store_strings[1];
		size = 84 + kssize;
		offset = off;
		
	}
	
	public void dump(){
		System.out.println("\nOAT-HEADER--------------------------------->");
		System.out.println("\tVersion:\t\t" + 
		new String(Arrays.copyOfRange(version.data, 0, 3), StandardCharsets.UTF_8));
		System.out.print("\tSize:\t\t\t");
		System.out.printf("0x%08X\n", size);
		System.out.println("\tHeader Checksum:\t" +
		Convertions.bytesToInt(adler32_checksum.data, 0, adler32_checksum.bSize));
		System.out.print("\tInstruction Set:\t");
		switch (Convertions.bytesToInt(instruction_set.data, 0, instruction_set.bSize)){
		case 0:
			System.out.println("Unspecified");
			break;
		case 1:
			System.out.println("ARM");
			break;
		case 2:
			System.out.println("ARM_64");
			break;
		case 3:
			System.out.println("Thumb-2");
			break;
		case 4:
			System.out.println("X86");
			break;
		case 5:
			System.out.println("X64");
			break;
		case 6:
			System.out.println("MIPS");
			break;
		case 7:
			System.out.println("MIPS_64");
			break;
		}
		System.out.println("\tDex-Files:\t\t" +
		Convertions.bytesToInt(dex_file_count.data, 0, dex_file_count.bSize));
		System.out.print("\tExe Offset(oatdata):\t");
		System.out.printf("0x%08X\n", Convertions.bytesToInt(executable_offset.data, 0, executable_offset.bSize));
		
		System.out.println("\tCreate-Arguments:\t"+creationArguments);
		System.out.println("END-OF-OAT-HEADER--------------------------<");
	}
	
	
	private String[] extractKeyPairStrings(){
		byte[] kvs = key_value_store.data;
		ArrayList<String> list = new ArrayList<String>();
		int border = 0;
		boolean emergencyBreak = false;
		for (int i = 0; i < kvs.length; i++){
			int c = 0;
			while(kvs[i] != 0x00){
				c++;
				i++;
				if (i > kvs.length){
					emergencyBreak = true;
					break;
				}
			}
			if (emergencyBreak){
				break;
			}
			list.add(new String(Arrays.copyOfRange(kvs,
					border, border+c)));
			border +=(c+1);
			c=0;
		}
		String[] ret = new String[list.size()];
		ret = list.toArray(ret);
		return ret;
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {version, adler32_checksum, instruction_set, instruction_set_features,
				dex_file_count, executable_offset, interpreter_to_interpreter_bridge_offset, 
				interpreter_to_compiled_code_bridge_offset, jni_dlsym_lookup_offset,
				portable_imt_conflict_trampoline_offset, portable_resolution_trampoline_offset,
				portable_to_interpreter_bridge_offset, quick_generic_jni_trampoline_offset,
				quick_imt_conflict_trampoline_offset, quick_resolution_trampoline_offset,
				quick_to_interpreter_bridge_offset, image_patch_delta, image_file_location_oat_checksum,
				image_file_location_oat_data_begin, key_value_store_size, key_value_store_size};
		
		byte[]bytes = new byte[size];
		
		int bp = 0;
		for (int i = 0; i < bd.length; i++){
			for (int j = 0; j < bd[i].bSize; j++){
				bytes[bp++] = bd[i].data[j];
			}
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
