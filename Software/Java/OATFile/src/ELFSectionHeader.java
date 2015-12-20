
public class ELFSectionHeader {
	public BData name; 		//index into string table
	public String sName;	//String of section name
	public BData type; 		//section content and semantics
	public BData flags;		//1 bit flags
	public BData addr;		//
	public BData offset;	//byte offset from filebegin to section
	public BData size;		//section size in Bytes
	public BData link;		//section header table index link
	public BData info;		//extra information about section
	public BData addralign;	//section alignment constraints
	public BData entsize;	//fixed size entries in table? 0 if not
	
	public ELFSectionHeader(byte[]src, int off){
		name = new BData(off + 0, new byte[]{src[off+0],src[off+1],src[off+2],src[off+3]});
		type = new BData(off + 4, new byte[]{src[off+4],src[off+5],src[off+6],src[off+7]});
		flags = new BData(off + 8, new byte[]{src[off+8],src[off+9],src[off+10],src[off+11]});
		addr = new BData(off + 12, new byte[]{src[off+12],src[off+13],src[off+14],src[off+15]});
		offset = new BData(off + 16, new byte[]{src[off+16],src[off+17],src[off+18],src[off+19]});
		size = new BData(off + 20, new byte[]{src[off+20],src[off+21],src[off+22],src[off+23]});
		link = new BData(off + 24, new byte[]{src[off+24],src[off+25],src[off+26],src[off+27]});
		info = new BData(off + 28, new byte[]{src[off+28],src[off+29],src[off+30],src[off+31]});
		addralign = new BData(off + 32, new byte[]{src[off+32],src[off+33],src[off+34],src[off+35]});
		entsize = new BData(off + 36, new byte[]{src[off+36],src[off+37],src[off+38],src[off+39]});
	}
	
	public void dump(){
		System.out.println("\n\tELF SECTION HEADER-------------------------->");
		System.out.println("\tSection Name: \t\t\t" + sName);
		System.out.print("\tSection Attributes\t\t");
		
		switch(Convertions.bytesToInt(flags.data, 0, flags.bSize)){
		case 1:
			System.out.println("Writeable");
			break;
		case 2:
			System.out.println("Allocation Area");
			break;
		case 4:
			System.out.println("Executable Machine Instructions");
			break;
		case 0xf0000000:
			System.out.println("Processor Semantics");
			break;
		default:
			System.out.println("Unknown");	
		}
		
		System.out.print("\tSection Content: \t\t");
		switch(Convertions.bytesToInt(type.data, 0, type.bSize)){
		case 0:
			System.out.println("Section Inactive");
			break;
		case 1:
			System.out.println("Defined by Program");
			break;
		case 2:
			System.out.println("Symtab");
			break;
		case 3:
			System.out.println("Stringtable");
			break;
		case 4:
			System.out.println("Relocation Entries");
			break;
		case 5:
			System.out.println("Symbol Hash Table");
			break;
		case 6:
			System.out.println("Info for Dynamic Linking");
			break;
		case 7:
			System.out.println("Marks the file in some way");
			break;
		case 8:
			System.out.println("Occupies no bits");
			break;
		case 9:
			System.out.println("Relocation Entries");
			break;
		case 10:
			System.out.println("Reserved, undefined semantics");
			break;
		case 11:
			System.out.println("Symbol Table");
			break;
		case 0x70000000:
			System.out.println("Processor Semantics");
			break;
		case 0x7fffffff:
			System.out.println("Processor Semantics");
			break;
		case 0x80000000:
			System.out.println("Lower Bound of Range Indexes");
			break;
		case 0xffffffff:
			System.out.println("Upper Bound of Range Indexes");
			break;
		}
		System.out.print("\tOffset to Section:\t\t");
		System.out.printf("0x%08X\n",Convertions.bytesToInt(offset.data, 0, offset.bSize));
		System.out.print("\tSection Size:\t\t\t");
		System.out.printf("0x%08X\n", + Convertions.bytesToInt(size.data, 0, size.bSize));
		
		switch(Convertions.bytesToInt(entsize.data, 0, entsize.bSize)){
		case 0:
			System.out.println("\tFixed-size Entry Table:\t\tFalse");
			break;
		default:
			System.out.println("\tFixed-size Entry Table:\t\tTrue");
		}
		System.out.println("\n\tEND OF ELF SECTION HEADER-------------------<");
	}
	
}
