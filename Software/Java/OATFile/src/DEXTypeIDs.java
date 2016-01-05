
/*
 * gives indexes in string_ids which strings corresponding
 * to a descriptor type
 * 
 */

public class DEXTypeIDs extends Section{

	public int offset;
	public int size;
	
	public DEXTypeIDItem[] type_id_items;
	
	public DEXTypeIDs(byte[] src, int off, int size) {
		this.size = size * 4;
		this.offset = off;
		type_id_items = new DEXTypeIDItem[size];
		for(int i = 0; i < type_id_items.length; i++){
			type_id_items[i] = new DEXTypeIDItem(src, off + i*4);
		}
	}
	
	@Override
	public void dump() {
		System.out.println("|--------Type IDs");
		System.out.print("|------------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Size:\t");
		System.out.printf("0x%08X\n", size);
		for(int i = 0; i < type_id_items.length; i++){
			type_id_items[i].dump();
		}
		System.out.println("|--------Type IDs");
	}

	@Override
	public byte[] getBytes() {
		byte [] b = new byte[size];
		int bp = 0;
		for (int i = 0; i < type_id_items.length; i++){
			byte[] tmp = type_id_items[i].getBytes();
			for (int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
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
		this.offset = off;
	}

}
