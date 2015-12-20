
public class ELFSymbolTable {
	
	private static final int entrySize = 16;
	public ELFSymbolTableEntry[] entries;
	
	public ELFSymbolTable(byte[]src, int off, int size, int stoff, int stsize){
		int nEntries = size/entrySize;
		entries = new ELFSymbolTableEntry[nEntries];
		ELFStringTable stringTable = new ELFStringTable(src, stoff, stsize);
		for (int i = 0; i < nEntries; i++){
			entries[i] = new ELFSymbolTableEntry(src, off+(i*entrySize));
			entries[i].sName = stringTable.
					getString(Convertions.bytesToInt(entries[i].name.data,
							0, entries[i].name.bSize));
		}
	}
	
	public void dump(){
		System.out.println("\nSYMBOL TABLE------------------------------------------------->");
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("\nEND OF SYMBOL TABLE------------------------------------------<");
	}
}
