
public class DEXFieldIDs extends Section{
	public int offset;
	public int size;
	
	public DEXFieldIDItem[] field_id_items;
	
	public DEXFieldIDs(byte[] src, int off, int size){
		this.size = size * 8;
		this.offset = off;
		field_id_items = new DEXFieldIDItem[size];
		for(int i = 0; i < field_id_items.length; i++){
			field_id_items[i] = new DEXFieldIDItem(src, off + i*8);
		}
	}
	
	@Override
	public void dump() {
		System.out.println("|--------Field IDs:");
		System.out.print("|------------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Size:\t");
		System.out.printf("0x%08X\n", size);
		for(int i = 0; i < field_id_items.length; i++){
			field_id_items[i].dump();
		}
		System.out.println("|--------Field IDs:");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[this.size];
		int bp = 0;
		for (int i = 0; i < field_id_items.length; i++){
			byte[] t = field_id_items[i].getBytes();
			for (int j = 0; j < t.length; j++){
				b[bp++] = t[j];
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
