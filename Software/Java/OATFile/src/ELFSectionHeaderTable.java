
public class ELFSectionHeaderTable extends Section{
	ELFSectionHeader [] entries;
	ELFStringTable strtab;
	ELFSectionHeader strtabheader;
	
	private int size;
	private int offset;
	
	public ELFSectionHeaderTable(byte[] src, int off,
			int nEntries, int entrySize, int strtabindex){
		
		entries = new ELFSectionHeader[nEntries]; //-StringTable
		
		strtabheader = new ELFSectionHeader(src, off+(strtabindex*entrySize));
		strtab = new ELFStringTable(src, strtabheader.boffset.getInt(),
				strtabheader.bsize.getInt());
			
		size = 0;
		for (int i = 0; i < nEntries; i++){
			entries[i] = new ELFSectionHeader(src, off+(i*entrySize));
			entries[i].sName = strtab.getString(entries[i].name.getInt());
			size+=entries[i].getSize();
		}
		offset = off;
	}
	
	public void dump(){
		System.out.println("|");
		System.out.println("|--ELF Section Header Table");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		System.out.println("|----Number of Entries: " + entries.length);
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("|--ELF Section Header Table");
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

	@Override
	public void setOffset(int off) {
		offset = off;	
	}
}
