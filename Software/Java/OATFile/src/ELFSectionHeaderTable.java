
public class ELFSectionHeaderTable {
	ELFSectionHeader [] entries;
	ELFStringTable stringTable;
	ELFSectionHeader stringTableHeader;
	
	public ELFSectionHeaderTable(byte[] src, int offset,
			int nEntries, int entrySize, int strTableIndex){
		
		entries = new ELFSectionHeader[nEntries]; //-StringTable
		
		stringTableHeader = new ELFSectionHeader(src, offset+(strTableIndex*entrySize));
		stringTable = new ELFStringTable(src,
				Convertions.bytesToInt(stringTableHeader.offset.data,0,stringTableHeader.offset.bSize),
				Convertions.bytesToInt(stringTableHeader.size.data, 0, stringTableHeader.size.bSize));
		for (int i = 0; i < nEntries; i++){
			entries[i] = new ELFSectionHeader(src, offset+(i*entrySize));
			entries[i].sName = stringTable.getString(Convertions.bytesToInt(entries[i].name.data, 0, entries[i].name.bSize));
		}
	}
	
	public void dump(){
		System.out.println("\nELF-SECTION-HEADER-TABLE ------------------------------------>");
		System.out.println("Number of Entries: " + entries.length);
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("\nEND OF ELF-SECTION-HEADER-TABLE -----------------------------<");
	}
}
