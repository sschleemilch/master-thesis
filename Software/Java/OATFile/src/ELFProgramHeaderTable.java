
public class ELFProgramHeaderTable extends ELFSection{
	
	public ELFProgramHeader[] entries;
	
	private int offset;
	private int size;
	
	public ELFProgramHeaderTable(byte[]src, int off, int nEntries, int entrySize){
		entries = new ELFProgramHeader[nEntries];
		for (int i = 0; i < entries.length; i++){
			entries[i] = new ELFProgramHeader(src, off + (i*entrySize));
		}
		size = 0;
		for (int i = 0; i < entries.length; i++){
			size += entries[i].getSize();
		}
		offset = off;
		
	}
	public void dump(){
		System.out.println("|");
		System.out.println("|--Program Header Table");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("|--Program Header Table");
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
