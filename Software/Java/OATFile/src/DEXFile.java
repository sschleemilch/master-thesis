import java.util.Arrays;

public class DEXFile extends Section{
	
	public DEXHeader header;
	public DEXStringIDs string_ids;
	public DEXTypeIDs type_ids;
	public DEXProtoIDs proto_ids;
	public DEXFieldIDs field_ids;
	public DEXMethodIDs method_ids;
	public DEXClassDefs class_defs;
	public DEXData data;
	public DEXLinkData link_data;
	
	private int size;
	private int offset;
	
	public Section[] sections = new Section[9];
	
	
	public DEXFile(byte[] src, int off){
		offset = off;
		header = new DEXHeader(src, off);
		size = header.file_size.getInt();
		string_ids = new DEXStringIDs(src, off + header.string_ids_off.getInt(), header.string_ids_size.getInt());
		string_ids = new DEXStringIDs(src, off + header.string_ids_off.getInt(), header.string_ids_size.getInt());
		type_ids = new DEXTypeIDs(src, off + header.type_ids_off.getInt(), header.type_ids_size.getInt());
		proto_ids = new DEXProtoIDs(src, off + header.proto_ids_off.getInt(), header.proto_ids_size.getInt());
		field_ids = new DEXFieldIDs(src, off + header.field_ids_off.getInt(), header.field_ids_size.getInt());
		method_ids = new DEXMethodIDs(src, off + header.method_ids_off.getInt(), header.method_ids_size.getInt());
		class_defs = new DEXClassDefs(src, off + header.class_defs_off.getInt(), header.class_defs_size.getInt());
		data = new DEXData(src, off + header.data_off.getInt(), header.data_size.getInt());
		link_data = new DEXLinkData(src, off + header.link_off.getInt(), header.link_size.getInt());
		
		sections[0] = header;
		sections[1] = string_ids;
		sections[2] = type_ids;
		sections[3] = proto_ids;
		sections[4] = field_ids;
		sections[5] = method_ids;
		sections[6] = class_defs;
		sections[7] = data;
		sections[8] = link_data;
	}
	
	public void dump(){
		System.out.println("|----Dex File");
		System.out.print("|--------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|--------Size:\t\t");
		System.out.printf("0x%08X\n", size);
		for (int i = 0; i < sections.length; i++){
			System.out.println();
			sections[i].dump();
		}
		System.out.println("|----Dex File");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[size];
		int bp = 0;
		for (int i = 0; i < sections.length; i++){
			byte [] sb = sections[i].getBytes();
			for (int j = 0; j < sb.length; j++){
				b[bp++] = sb[j];
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
		return offset;
	}

	@Override
	public void setOffset(int off) {
		offset = off;
	}
}
