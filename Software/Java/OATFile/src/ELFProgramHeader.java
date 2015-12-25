
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
		System.out.println("\tPROGRAM HEADER --------------------->");
		System.out.print("\tType:\t\t");
		switch(Convertions.bytesToInt(type.data, 0, type.bSize)){
		case 0:
			System.out.println("Unused");
			break;
		case 1:
			System.out.println("Loadable Segment");
			break;
		case 2:
			System.out.println("Dynamic Linking Info");
			break;
		case 3:
			System.out.println("Path Name to Invoke");
			break;
		case 4:
			System.out.println("Auxilialry Info");
			break;
		case 5:
			System.out.println("Reserved, Unspecified Semantics");
			break;
		case 6:
			System.out.println("Location + Size of PHTable");
			break;
		case 0x70000000:
			System.out.println("CPU Semantics");
			break;
		case 0x7fffffff:
			System.out.println("CPU Semantics");
			break;
		}
		System.out.print("\tOffset:\t\t");
		System.out.printf("0x%08X\n", Convertions.bytesToInt(offset.data, 0, offset.bSize));
		System.out.print("\tVirtAddress:\t");
		System.out.printf("0x%08X\n", Convertions.bytesToInt(vaddr.data, 0, vaddr.bSize));
		System.out.print("\tPhysAddress:\t");
		System.out.printf("0x%08X\n", Convertions.bytesToInt(paddr.data, 0, paddr.bSize));
		System.out.println("\tFilesize:\t" + Convertions.bytesToInt(filesz.data, 0, filesz.bSize));
		System.out.println("\tMemsize:\t" + Convertions.bytesToInt(memsz.data, 0, memsz.bSize));
		System.out.println("\tAlignment:\t" + Convertions.bytesToInt(align.data, 0, align.bSize));
		System.out.println("\tEND OF PROGRAM HEADER --------------<");
	}
	
}
