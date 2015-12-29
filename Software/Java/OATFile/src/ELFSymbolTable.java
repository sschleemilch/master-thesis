
public class ELFSymbolTable extends Section{
	
	private static final int entrySize = 16;
	public ELFSymbolTableEntry[] entries;
	
	private int size;
	private int offset;
	
	public ELFSymbolTable(byte[]src, int off, int size, int stoff, int stsize){
		int nEntries = size/entrySize;
		entries = new ELFSymbolTableEntry[nEntries];
		ELFStringTable stringTable = new ELFStringTable(src, stoff, stsize);
		for (int i = 0; i < entries.length; i++){
			entries[i] = new ELFSymbolTableEntry(src, off+(i*entrySize));
			entries[i].sName = stringTable.getString(entries[i].name.getInt());
		}
		this.size = 0;
		for (int i = 0; i < entries.length; i++){
			this.size += entries[i].getSize();
		}
		offset = off;
	}
	
	public void dump(){
		System.out.println("|");
		System.out.println("|--Symbol Table");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("|--Symbol Table");
	}

	@Override
	public byte[] getBytes() {
		byte[] bytes = new byte[size];
		int bp = 0;
		for(int i = 0; i < entries.length; i++){
			byte[]btmp = entries[i].getBytes();
			for (int j = 0; j < btmp.length; j++){
				bytes[bp++] = btmp[j];
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

	@Override
	public void setOffset(int off) {
		offset = off;	
	}
}
