
public class ELFSymbolTable extends ELFSection{
	
	private static final int entrySize = 16;
	public ELFSymbolTableEntry[] entries;
	
	private int size;
	private int offset;
	
	public ELFSymbolTable(byte[]src, int off, int size, int stoff, int stsize){
		int nEntries = size/entrySize;
		entries = new ELFSymbolTableEntry[nEntries];
		ELFSectionHeaderStringTable stringTable = new ELFSectionHeaderStringTable(src, stoff, stsize);
		for (int i = 0; i < nEntries; i++){
			entries[i] = new ELFSymbolTableEntry(src, off+(i*entrySize));
			entries[i].sName = stringTable.
					getString(Convertions.bytesToInt(entries[i].name.data,
							0, entries[i].name.bSize));
		}
		size = 0;
		for (int i = 0; i < entries.length; i++){
			size += entries[i].getSize();
		}
		offset = off;
	}
	
	public void dump(){
		System.out.println("\nSYMBOL TABLE------------------------------------------------->");
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("\nEND OF SYMBOL TABLE------------------------------------------<");
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
		return size;
	}
}
