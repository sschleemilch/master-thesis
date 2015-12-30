
public class DEXClassDefs extends Section{
	public int offset;
	public int size;
	public int dex_file_off;
	
	public DEXClassDefItem[] class_def_items;
	
	
	public DEXClassDefs(byte[]src, int off, int size, int dex_file_off) {
		this.dex_file_off = dex_file_off;
		this.offset = off;
		this.size = size*32;
		class_def_items = new DEXClassDefItem[size];
		for (int i = 0; i < size; i++){
			class_def_items[i] = new DEXClassDefItem(src, off + i*32, dex_file_off);
		}
	}
	
	@Override
	public void dump() {
		System.out.println("|--------Class Defs");
		System.out.print("|------------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Size:\t");
		System.out.printf("0x%08X\n", size);
		for (int i = 0; i < class_def_items.length; i++){
			class_def_items[i].dump();
		}
		System.out.println("|--------Class Defs");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[this.size];
		int bp = 0;
		for (int i = 0; i < class_def_items.length; i++){
			byte[] t = class_def_items[i].getBytes();
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
