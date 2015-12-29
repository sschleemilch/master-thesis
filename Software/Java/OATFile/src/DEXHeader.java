import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DEXHeader extends Section{
	public BData magic;
	public BData checksum;
	public BData signature;
	public BData file_size;
	public BData header_size;
	public BData endian_tag;
	public BData link_size;
	public BData link_off;
	public BData map_off;
	public BData string_ids_size;
	public BData string_ids_off;
	public BData type_ids_size;
	public BData type_ids_off;
	public BData proto_ids_size;
	public BData proto_ids_off;
	public BData field_ids_size;
	public BData field_ids_off;
	public BData method_ids_size;
	public BData method_ids_off;
	public BData class_defs_size;
	public BData class_defs_off;
	public BData data_size;
	public BData data_off;
	
	public String version;
	private int size;
	private int offset;
	
	public DEXHeader (byte[] src, int off){
		magic = new BData(off+0, new byte[]{src[off+0],src[off+1],
				src[off+2],src[off+3],src[off+4],src[off+5],src[off+6],
				src[off+7]});
		checksum = new BData(off+8, new byte[]{src[off+8],src[off+9],
				src[off+10],src[off+11]});
		signature = new BData(off+12, new byte[]{src[off+12],src[off+13],
				src[off+14],src[off+15],src[off+16],src[off+17],src[off+18],
				src[off+19], src[off+20], src[off+21], src[off+22], src[off+23],
				src[off+24], src[off+25], src[off+26], src[off+27], src[off+28],
				src[off+29], src[off+30], src[off+31]});
		file_size = new BData(off+32, new byte[]{src[off+32], src[off+33],
				src[off+34],src[off+35]});
		header_size = new BData(off+36, new byte[]{src[off+36], src[off+37],
				src[off+38],src[off+39]});
		endian_tag = new BData(off+40, new byte[]{src[off+40], src[off+41],
				src[off+42],src[off+43]});
		link_size = new BData(off+44, new byte[]{src[off+44], src[off+45],
				src[off+46],src[off+47]});
		link_off = new BData(off+48, new byte[]{src[off+48], src[off+49],
				src[off+50],src[off+51]});
		map_off = new BData(off+52, new byte[]{src[off+52], src[off+53],
				src[off+54],src[off+55]});
		string_ids_size = new BData(off+56, new byte[]{src[off+56], src[off+57],
				src[off+58],src[off+59]});
		string_ids_off = new BData(off+60, new byte[]{src[off+60], src[off+61],
				src[off+62],src[off+63]});
		type_ids_size = new BData(off+64, new byte[]{src[off+64], src[off+65],
				src[off+66],src[off+67]});
		type_ids_off = new BData(off+68, new byte[]{src[off+68], src[off+69],
				src[off+70],src[off+71]});
		proto_ids_size = new BData(off+72, new byte[]{src[off+72], src[off+73],
				src[off+74],src[off+75]});
		proto_ids_off = new BData(off+76, new byte[]{src[off+76], src[off+77],
				src[off+78],src[off+79]});
		field_ids_size = new BData(off+80, new byte[]{src[off+80], src[off+81],
				src[off+82],src[off+83]});
		field_ids_off = new BData(off+84, new byte[]{src[off+84], src[off+85],
				src[off+86],src[off+87]});
		method_ids_size = new BData(off+88, new byte[]{src[off+88], src[off+89],
				src[off+90],src[off+91]});
		method_ids_off = new BData(off+92, new byte[]{src[off+92], src[off+93],
				src[off+94],src[off+95]});
		class_defs_size = new BData(off+96, new byte[]{src[off+96], src[off+97],
				src[off+98],src[off+99]});
		class_defs_off = new BData(off+100, new byte[]{src[off+100], src[off+101],
				src[off+102],src[off+103]});
		data_size = new BData(off+104, new byte[]{src[off+104], src[off+105],
				src[off+106],src[off+107]});
		data_off = new BData(off+108, new byte[]{src[off+108], src[off+109],
				src[off+110],src[off+111]});
		
		size = magic.bSize + checksum.bSize + signature.bSize + file_size.bSize + 
				header_size.bSize + endian_tag.bSize + link_size.bSize + link_off.bSize +
				map_off.bSize + string_ids_size.bSize + string_ids_off.bSize + 
				type_ids_size.bSize + type_ids_off.bSize + proto_ids_size.bSize + proto_ids_off.bSize +
				field_ids_size.bSize + field_ids_off.bSize + method_ids_size.bSize + method_ids_off.bSize +
				class_defs_size.bSize + class_defs_off.bSize + data_size.bSize + data_off.bSize;
		
		offset = off;
		
		version = new String(Arrays.copyOfRange(magic.data, 4, 7), StandardCharsets.UTF_8);
	}
	public void dump(){
		System.out.println("|--------Dex Header");
		System.out.print("|------------Offset:\t\t\t");
		System.out.printf("0x%08X\n", offset);
		System.out.println("|------------Version:\t\t\t" + version);
		System.out.println("|------------File Size:\t\t\t" + file_size.getInt());
		System.out.println("|------------Header Size:\t\t" + header_size.getInt());
		System.out.print("|------------Endian Constant:\t\t");
		System.out.printf("0x%08X\n", endian_tag.getInt());
		System.out.println("|------------Link Size:\t\t\t" + link_size.getInt());
		System.out.print("|------------Link Offset:\t\t");
		System.out.printf("0x%08X\n", link_off.getInt());
		System.out.print("|------------Map Offset:\t\t");
		System.out.printf("0x%08X\n", map_off.getInt());
		System.out.print("|------------String Offset:\t\t");
		System.out.printf("0x%08X\n", string_ids_off.getInt());
		System.out.println("|------------N-Strings\t\t\t"+ string_ids_size.getInt());
		System.out.print("|------------Type IDs Offset:\t\t");
		System.out.printf("0x%08X\n", type_ids_off.getInt());
		System.out.println("|------------N-Type IDs\t\t\t"+ type_ids_size.getInt());
		System.out.print("|------------Proto IDs Offset:\t\t");
		System.out.printf("0x%08X\n", proto_ids_off.getInt());
		System.out.println("|------------N-Proto IDs\t\t"+ proto_ids_size.getInt());
		System.out.print("|------------Field IDs Offset:\t\t");
		System.out.printf("0x%08X\n", field_ids_off.getInt());
		System.out.println("|------------N-Field IDs\t\t"+ field_ids_size.getInt());
		System.out.print("|------------Method IDs Offset:\t\t");
		System.out.printf("0x%08X\n", method_ids_off.getInt());
		System.out.println("|------------N-Method IDs\t\t"+ method_ids_size.getInt());
		System.out.print("|------------Class Defs Offset:\t\t");
		System.out.printf("0x%08X\n", class_defs_off.getInt());
		System.out.println("|------------N-Class Defs\t\t"+ class_defs_size.getInt());
		System.out.print("|------------Data Section Offset:\t");
		System.out.printf("0x%08X\n", data_off.getInt());
		System.out.println("|------------Data Section Size:\t\t" + data_size.getInt());
		System.out.println("|--------Dex Header");
	}
	@Override
	public byte[] getBytes() {
		BData[] bd = {magic, checksum, signature, file_size, header_size, endian_tag, 
				link_size, link_off, map_off, string_ids_size, string_ids_off, 
				type_ids_size, type_ids_off, proto_ids_size, proto_ids_off, 
				field_ids_size, field_ids_off, method_ids_size, method_ids_off,
				class_defs_size, class_defs_off, data_size, data_off};
		byte[] b = new byte[size];
		int bp = 0;
		for (int i = 0; i < bd.length; i++){
			for(int j = 0; j < bd[i].bSize; j++){
				b[bp++] = bd[i].data[j];
			}
		}
		
		return b;
	}	
	
	@Override
	public int getSize() {
		return size;
	}
	@Override
	public int getOffset() {
		return size;
	}
	@Override
	public void setOffset(int off) {
		offset = off;
	}
}
