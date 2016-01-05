import java.util.Arrays;

public class DEXFile extends Section{
	
	public DEXHeader header;
	// All of these sections are referencing objects in the data section
	public DEXStringIDs string_ids;
	public DEXTypeIDs type_ids;
	public DEXProtoIDs proto_ids;
	public DEXFieldIDs field_ids;
	public DEXMethodIDs method_ids;
	public DEXClassDefs class_defs;
	
	public DEXData data;
	public DEXLinkData link_data;
	
	//The following are referenced from other sections
	//and do appear in the data section
	public DEXStringDataItem[] string_data_items;
	public DEXClassDataItem[] class_data_items;
	public DEXCodeItem[] code_items;


	private int size;
	private int offset;
	
	public Section[] sections = new Section[9];
	
	
	public byte[] bytes;
	
	
	public DEXFile(byte[] src, int off){
		offset = off;
		header = new DEXHeader(src, off);
		size = header.file_size.getInt();
		string_ids = new DEXStringIDs(src, off + header.string_ids_off.getInt(), header.string_ids_size.getInt());
		type_ids = new DEXTypeIDs(src, off + header.type_ids_off.getInt(), header.type_ids_size.getInt());
		proto_ids = new DEXProtoIDs(src, off + header.proto_ids_off.getInt(),
				header.proto_ids_size.getInt());
		field_ids = new DEXFieldIDs(src, off + header.field_ids_off.getInt(), header.field_ids_size.getInt());
		method_ids = new DEXMethodIDs(src, off + header.method_ids_off.getInt(), header.method_ids_size.getInt());
		class_defs = new DEXClassDefs(src, off + header.class_defs_off.getInt(),
				header.class_defs_size.getInt(), off);
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
		
		string_data_items = new DEXStringDataItem[string_ids.string_id_items.length];
		for (int i = 0; i < string_data_items.length; i++){
			string_data_items[i] = new DEXStringDataItem(src,
					off + string_ids.string_id_items[i].string_data_off.getInt());
		}
		
		class_data_items = new DEXClassDataItem[class_defs.class_def_items.length];
		for (int i = 0; i < class_data_items.length; i++){
			class_data_items[i] = new DEXClassDataItem(src,
					off + class_defs.class_def_items[i].class_data_off.getInt());
		}
		
		
		
		DEXEncodedMethod[] encoded_methods;
		//count how much methods there are...		
		int emc = 0;
		for (int i = 0; i < class_data_items.length; i++){
			emc += class_data_items[i].direct_methods.length;
			emc += class_data_items[i].virtual_methods.length;
		}
		encoded_methods = new DEXEncodedMethod[emc];
		//fill method array...
		int ep=0;
		for (int i = 0; i < class_data_items.length; i++){
			for (int j = 0; j < class_data_items[i].direct_methods.length; j++){
				encoded_methods[ep++] = class_data_items[i].direct_methods[j];
			}
			for (int j = 0; j < class_data_items[i].virtual_methods.length; j++){
				encoded_methods[ep++] = class_data_items[i].virtual_methods[j];
			}
		}
		code_items = new DEXCodeItem[encoded_methods.length];
		for(int i = 0; i < code_items.length; i++){
			code_items[i] = new DEXCodeItem(src, off + encoded_methods[i].code_off_uleb.getUleb128());
		}
		bytes = Arrays.copyOfRange(src, off, off + size);

	}
	
	public void deleteDEXInsns(){
		//delete all insns of every code_item
		for (int i = 0; i < code_items.length; i++){
			for(int j = 0; j < code_items[i].insns.length; j++){
				code_items[i].insns[j].setInt(0); //all instructions are set to 0
			}
		}
		//change actual bytes of data section...
		for (int i = 0; i < code_items.length; i++){
			byte[] btw = code_items[i].getBytes();
			int btwp = 0;
			//data section index = code_item_off - data_off
			int startindex = code_items[i].getOffset() - data.getOffset();
			for (int j = startindex; j < startindex + btw.length; j++){
				data.bytes[j] = btw[btwp++];
			}
		}
	}
	
	public void dump(){

		System.out.println("|----Dex File");
		System.out.print("|--------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|--------Size:\t\t");
		System.out.printf("0x%08X\n", size);
		header.dump();
		
		for (int i = 0; i < class_defs.class_def_items.length; i++){
			System.out.println("\n|--------Class Def");
			System.out.println("|------------Type:\t\t" + string_data_items
					[type_ids.type_id_items
					 [class_defs.class_def_items[i]
							 .class_idx.getInt()]
									 .descriptor_idx.getInt()]
							  .getString());
			System.out.println("|------------Source File:\t" + string_data_items
					[class_defs.class_def_items[i].source_file_idx.getInt()].getString());
			System.out.println("|------------N-Static Fields:\t\t" +
					class_data_items[i].static_fields_size_uleb.getUleb128());
			System.out.println("|------------N-Instance Fields:\t\t" +
					class_data_items[i].instance_fields_size_uleb.getUleb128());
			System.out.println("|------------N-Direct Methods:\t\t" +
					class_data_items[i].direct_method_size_uleb.getUleb128());
			System.out.println("|------------N-Virtual Methods:\t\t" +
					class_data_items[i].virtual_method_size_uleb.getUleb128());
			for ( int j = 0; j < class_data_items[i].static_fields_size_uleb.getUleb128(); j++){
				int fidindex = 0;
				if (j == 0){
					 fidindex = class_data_items[i]
							.static_fields[j]
							.field_idx_diff_uleb
							.getUleb128();
					
					
				}else{
					fidindex += class_data_items[i]
							.static_fields[j]
							.field_idx_diff_uleb
							.getUleb128();
				}
				System.out.println("|----------------Static Field");
				DEXAccessFlags af = new DEXAccessFlags(class_data_items[i]
						.static_fields[j].access_flags_uleb.getUleb128());
				System.out.println("|--------------------Access:\t" + af.flag_str );
				System.out.println("|--------------------Name:\t" + string_data_items[field_ids.field_id_items[fidindex].name_idx.getInt()].getString());
				System.out.println("|--------------------Type:\t" +
						string_data_items[
				                type_ids.type_id_items[field_ids
				                        .field_id_items[fidindex]
				                		.type_idx.getInt()]
				                		.descriptor_idx.getInt()]
				                		.getString());
				System.out.println("|----------------Static Field");
			}
			for ( int j = 0; j < class_data_items[i].instance_fields_size_uleb.getUleb128(); j++){
				int fidindex = 0;
				if (j == 0){
					 fidindex = class_data_items[i]
							.instance_fields[j]
							.field_idx_diff_uleb
							.getUleb128();
					
					
				}else{
					fidindex += class_data_items[i]
							.instance_fields[j]
							.field_idx_diff_uleb
							.getUleb128();
				}
				System.out.println("|----------------Instance Field");
				DEXAccessFlags af = new DEXAccessFlags(class_data_items[i]
						.static_fields[j].access_flags_uleb.getUleb128());
				System.out.println("|--------------------Access:\t" + af.flag_str );
				System.out.println("|--------------------Name:\t" + string_data_items[field_ids.field_id_items[fidindex].name_idx.getInt()].getString());
				System.out.println("|--------------------Type:\t" +
						string_data_items[
				                type_ids.type_id_items[field_ids
				                        .field_id_items[fidindex]
				                		.type_idx.getInt()]
				                		.descriptor_idx.getInt()]
				                		.getString());
				System.out.println("|----------------Instance Field");
			}
			for (int j = 0; j < class_data_items[i].direct_method_size_uleb.getUleb128(); j++){
			
				System.out.println("|----------------Direct Method");
				DEXAccessFlags af = new DEXAccessFlags(class_data_items[i]
						.direct_methods[j].access_flags_uleb.getUleb128());
				System.out.println("|--------------------Access:\t\t" + af.flag_str );
				DEXCodeItem ci = new DEXCodeItem(bytes, class_data_items[i]
						.direct_methods[j].code_off_uleb.getUleb128());
				ci.dump();
				System.out.println("|----------------Direct Method");
			}
			for (int j = 0; j < class_data_items[i].virtual_method_size_uleb.getUleb128(); j++){
		
				System.out.println("|----------------Virtual Method");
				DEXAccessFlags af = new DEXAccessFlags(class_data_items[i]
						.virtual_methods[j].access_flags_uleb.getUleb128());
				System.out.println("|--------------------Access:\t\t" + af.flag_str );
				DEXCodeItem ci = new DEXCodeItem(bytes,class_data_items[i]
						.virtual_methods[j].code_off_uleb.getUleb128());
				ci.dump();
				System.out.println("|----------------Virtual Method");
			}
			
			
			System.out.println("|--------Class Def");
		}
		
		System.out.println("|----Dex File");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[size];
		int bp = 0;
		for (int i = 0; i < sections.length; i++){
			byte [] sb = sections[i].getBytes();
			if (sb.length != sections[i].getSize()){
				System.out.println("Index: " + i);
			}
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
