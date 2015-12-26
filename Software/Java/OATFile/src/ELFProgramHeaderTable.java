
public class ELFProgramHeaderTable extends ELFSection{
	
	public ELFProgramHeader[] entries;
	public int offset;
	public int size;
	
	public ELFProgramHeaderTable(byte[]src, int off, int nEntries, int entrySize){
		entries = new ELFProgramHeader[nEntries];
		for (int i = 0; i < entries.length; i++){
			entries[i] = new ELFProgramHeader(src, off + (i*entrySize));
		}
		size = 0;
		for (int i = 0; i < entries.length; i++){
			size += entries[i].size;
		}
		offset = off;
		
	}
	public void dump(){
		System.out.println("\nPROGRAM HEADER TABLE ---------------------------------------->");
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("\nEND OF PROGRAM HEADER TABLE ---------------------------------<");
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
	
}
