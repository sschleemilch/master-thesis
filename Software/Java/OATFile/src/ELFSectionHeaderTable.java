
public class ELFSectionHeaderTable extends ELFSection{
	ELFSectionHeader [] entries;
	ELFSectionHeaderStringTable stringTable;
	ELFSectionHeader stringTableHeader;
	
	private int size;
	private int offset;
	
	public ELFSectionHeaderTable(byte[] src, int off,
			int nEntries, int entrySize, int strTableIndex){
		
		entries = new ELFSectionHeader[nEntries]; //-StringTable
		
		stringTableHeader = new ELFSectionHeader(src, offset+(strTableIndex*entrySize));
		stringTable = new ELFSectionHeaderStringTable(src,
				Convertions.bytesToInt(stringTableHeader.boffset.data,0,stringTableHeader.boffset.bSize),
				Convertions.bytesToInt(stringTableHeader.bsize.data, 0, stringTableHeader.bsize.bSize));
		size = 0;
		for (int i = 0; i < nEntries; i++){
			entries[i] = new ELFSectionHeader(src, offset+(i*entrySize));
			entries[i].sName = stringTable.getString(Convertions.bytesToInt(entries[i].name.data,
					0, entries[i].name.bSize));
			size+=entries[i].getSize();
		}
		offset = off;
	}
	
	public void dump(){
		System.out.println("\nELF-SECTION-HEADER-TABLE ------------------------------------>");
		System.out.println("Number of Entries: " + entries.length);
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("\nEND OF ELF-SECTION-HEADER-TABLE -----------------------------<");
	}

	@Override
	public byte[] getBytes() {
		byte[] bytes = new byte[size];
		int bp =0;
		for (int i = 0; i < entries.length; i++){
			byte[]tmp = entries[i].getBytes();
			for (int j = 0; j < tmp.length; j++){
				bytes[bp++] = tmp[j];
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
}
