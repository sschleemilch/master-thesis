
public class DEXClassDefs extends Section{
	public int offset;
	public int size;
	
	public DEXClassDefItem[] entries;
	
	
	public DEXClassDefs(byte[]src, int off, int size) {
		this.offset = off;
		this.size = size*32;
		entries = new DEXClassDefItem[size];
		for (int i = 0; i < size; i++){
			entries[i] = new DEXClassDefItem(src, off + i*32);
		}
	}
	
	@Override
	public void dump() {
		System.out.println("|--------Class Defs");
		System.out.print("|------------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Size:\t");
		System.out.printf("0x%08X\n", size);
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("|--------Class Defs");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[this.size];
		int bp = 0;
		for (int i = 0; i < entries.length; i++){
			byte[] t = entries[i].getBytes();
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
