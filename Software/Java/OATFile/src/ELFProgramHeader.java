
public class ELFProgramHeader {
	public BData type;
	public BData offset;
	public BData vaddr;
	public BData paddr;
	public BData filesz;
	public BData memsz;
	public BData flags;
	public BData align;
	
	public ELFProgramHeader(byte[]src, int off){
		type = new BData(off + 0, new byte[]{src[off + 0], src[off + 1],
				src[off + 2], src[off + 3]});
		offset = new BData(off + 4, new byte[]{src[off + 4], src[off + 5],
				src[off + 6], src[off + 7]});
		vaddr = new BData(off + 8, new byte[]{src[off + 8], src[off + 9],
				src[off + 10], src[off + 11]});
		paddr = new BData(off + 12, new byte[]{src[off + 12], src[off + 13],
				src[off + 14], src[off + 15]});
		filesz = new BData(off + 16, new byte[]{src[off + 16], src[off + 17],
				src[off + 18], src[off + 19]});
		memsz = new BData(off + 20, new byte[]{src[off + 20], src[off + 21],
				src[off + 22], src[off + 23]});
		flags = new BData(off + 24, new byte[]{src[off + 24], src[off + 25],
				src[off + 26], src[off + 27]});
		align = new BData(off + 28, new byte[]{src[off + 28], src[off + 29],
				src[off + 30], src[off + 31]});
	}
	
	public void dump(){
		System.out.println("PROGRAM HEADER --------------------->");
		
		System.out.println("END OF PROGRAM HEADER --------------<");
	}
	
}
