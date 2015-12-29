
public class ELFProgramHeader extends Section{
	public BData type;
	public BData boffset;
	public BData vaddr;
	public BData paddr;
	public BData filesz;
	public BData memsz;
	public BData flags;
	public BData align;
	
	private int size;
	private int offset;
	
	public ELFProgramHeader(byte[]src, int off){
		type = new BData(off + 0, new byte[]{src[off + 0], src[off + 1],
				src[off + 2], src[off + 3]});
		boffset = new BData(off + 4, new byte[]{src[off + 4], src[off + 5],
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
		
		offset = off;
		size = type.bSize + boffset.bSize + vaddr.bSize + 
				paddr.bSize + filesz.bSize + memsz.bSize +
				flags.bSize + align.bSize;
	}
	
	public void dump(){
		System.out.println("|----Program Header");
		System.out.print("|--------Type:\t\t");
		switch(type.getInt()){
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
		System.out.print("|--------Offset:\t");
		System.out.printf("0x%08X\n", boffset.getInt());
		System.out.print("|--------VirtAddress:\t");
		System.out.printf("0x%08X\n", vaddr.getInt());
		System.out.print("|--------PhysAddress:\t");
		System.out.printf("0x%08X\n", paddr.getInt());
		System.out.println("|--------Filesize:\t" + filesz.getInt());
		System.out.println("|--------Memsize:\t" + memsz.getInt());
		System.out.println("|--------Alignment:\t" + align.getInt());
		System.out.println("|----Program Header");
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {type, boffset, vaddr, paddr, filesz, memsz, flags, align};
		byte[] b = new byte[size];
		int bp = 0;
		for (int i = 0; i < bd.length; i++){
			for(int j = 0; j < bd[i].bSize; j++){
				b[bp++] = bd[i].data[j];
			}
		}
		
		return b;
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
