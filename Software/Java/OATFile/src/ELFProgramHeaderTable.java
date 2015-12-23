
public class ELFProgramHeaderTable {
	
	public ELFProgramHeader[] entries;
	
	public ELFProgramHeaderTable(byte[]src, int off, int nEntries, int entrySize){
		entries = new ELFProgramHeader[nEntries];
		for (int i = 0; i < entries.length; i++){
			entries[i] = new ELFProgramHeader(src, off + (i*entrySize));
		}
	}
	public void dump(){
		System.out.println("PROGRAM HEADER TABLE --------------------->");
		for (int i = 0; i < entries.length; i++){
			entries[i].dump();
		}
		System.out.println("END OF PROGRAM HEADER TABLE --------------<");
	}
	
}
