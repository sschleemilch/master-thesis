
public class ELFHeader extends ELFSection{
	//ident-----------------16Bytes
	public BData iMagic0 = new BData(0,new byte[]{0x7f});
	public BData iMagic1 = new BData(1,new byte[]{'E'});
	public BData iMagic2 = new BData(2,new byte[]{'L'});
	public BData iMagic3 = new BData(3,new byte[]{'F'});
	public BData iFileBitClass; //32,64 Bit or None
	public BData iDataEncoding; //LSB/MSB/None
	public BData iHVersion; //Header Version Number
	public BData iPadding; //Beginning of unused bytes in ident
	//-------------------------
	public BData type; //object file type
	public BData machine; //required architecture
	public BData fVersion; //object file version
	public BData entry; //virtual address to first control overtake
	public BData phoff; //program header table offset
	public BData shoff; //section header table offset
	public BData flags; //processor flags
	public BData ehsize; // elf header size
	public BData phentsize; //size of 1 entry in program header table
	public BData phnum; // n-entries in program header table
	public BData shentsize; //size of 1 entry in section header table
	public BData shnum; //n-entries in section header table
	public BData shstrndx; //section header string table index
	
	private int size;
	private int offset;
	
	private boolean isELFFile(byte[]src){
		if (src[0] == iMagic0.data[0] &&
				src[1] == iMagic1.data[0] &&
				src[2] == iMagic2.data[0] &&
				src[3] == iMagic3.data[0]){
			return true;
		}else{
			return false;
		}
	}
	
	public ELFHeader(byte[]src){
		if (isELFFile(src)){
			iFileBitClass = new BData(4, new byte[]{src[4]});
			iDataEncoding = new BData(5, new byte[]{src[5]});
			iHVersion = new BData(6, new byte[]{src[6]});
			iPadding = new BData(7, new byte[]{src[7]});
			
			type = new BData(16, new byte[]{src[16],src[17]});
			machine = new BData(18, new byte[]{src[18],src[19]});
			fVersion = new BData(20, new byte[]{src[20],src[21],src[22],src[23]});
			entry = new BData(24, new byte[]{src[24],src[25],src[26],src[27]});
			phoff = new BData(28, new byte[]{src[28],src[29],src[30],src[31]});
			shoff = new BData(32, new byte[]{src[32],src[33],src[34],src[35]});
			flags = new BData(36, new byte[]{src[36],src[37],src[38],src[39]});
			ehsize = new BData(40, new byte[]{src[40],src[41]});
			phentsize = new BData(42, new byte[]{src[42],src[43]});
			phnum = new BData(44, new byte[]{src[44],src[45]});
			shentsize = new BData(46, new byte[]{src[46],src[47]});
			shnum = new BData(48, new byte[]{src[48],src[49]});
			shstrndx = new BData(50, new byte[]{src[50],src[51]});
			
			size = Convertions.bytesToInt(ehsize.data, 0, ehsize.bSize);
			offset = 0;
		}
	}
	
	public void dump(){
		System.out.println("|");
		System.out.println("|--ELF-Header");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		System.out.print("|----File Bit Class: \t\t\t\t\t");
		switch(iFileBitClass.data[0]){
		case 0:
			System.out.println("INVALID");
			break;
		case 1:
			System.out.println("32Bit");
			break;
		case 2:
			System.out.println("64Bit");
			break;
		}
		System.out.print("|----Encoding obj-File:\t\t\t\t\t");
		switch(iDataEncoding.data[0]){
		case 0:
			System.out.println("None");
			break;
		case 1:
			System.out.println("LSB");
			break;
		case 2:
			System.out.println("MSB");
			break;
		}
		System.out.println("|----Header Version: \t\t\t\t\t"+iHVersion.data[0]);
		System.out.println("|----Unused Bytes from:\t\t\t\t\t" + iPadding.data[0]);;
		
		System.out.print("|----Object File Type:\t\t\t\t\t");
		switch(Convertions.bytesToInt(type.data, 0, type.bSize)){
		case 0:
			System.out.println("NONE");
			break;
		case 1:
			System.out.println("RELOCATABLE");
			break;
		case 2:
			System.out.println("EXECUTABLE");
			break;
		case 3:
			System.out.println("SHARED OBJECT");
			break;
		case 4:
			System.out.println("CORE FILE");
			break;
		case 0xff00:
			System.out.println("PROCESSOR SPECIFIC");
			break;
		case 0xffff:
			System.out.println("PROCESSOR SPECIFIC");
			break;
			
		}
		
		System.out.print("|----Required Architecture:\t\t\t\t");
		switch(machine.data[0]){
		case 0:
			System.out.println("NO MACHINE");
			break;
		case 1:
			System.out.println("AT&T");
			break;
		case 2:
			System.out.println("SPARC");
			break;
		case 3:
			System.out.println("INTEL 80386");
			break;
		case 4:
			System.out.println("MOTOROLA 68000");
			break;
		case 5:
			System.out.println("MOTOROLA 88000");
			break;
		case 6:
			System.out.println("NONE");
			break;
		case 7:
			System.out.println("INTEL 80860");
			break;
		case 8:
			System.out.println("MIPS RS3000");
			break;
		default:
			System.out.println("ARM?");
		}
		System.out.println("|----Object File Version: \t\t\t\t" 
		+ Convertions.bytesToInt(fVersion.data,
				0, fVersion.bSize));
		System.out.print("|----Virtual Entry Address:\t\t\t\t");
		System.out.printf("0x%08X\n",Convertions.bytesToInt(entry.data, 0, entry.bSize));
		System.out.print("|----Program Header Table Offset:\t\t\t");
		System.out.printf("0x%08X\n",Convertions.bytesToInt(phoff.data, 0, phoff.bSize));
		System.out.println("|----Size of Program Header Table Entry: \t\t" 
		+ Convertions.bytesToInt(phentsize.data,
				0, phentsize.bSize));
		System.out.println("|----Number of Entries in Program Header Table: \t" 
				+ Convertions.bytesToInt(phnum.data,
						0, phnum.bSize));
		System.out.print("|----Section Header Table Offset:\t\t\t");
		System.out.printf("0x%08X\n",Convertions.bytesToInt(shoff.data, 0, shoff.bSize));
		System.out.println("|----Size of Section Header Table Entry: \t\t" 
				+ Convertions.bytesToInt(shentsize.data,
						0, shentsize.bSize));
		System.out.println("|----Number of Entries in Section Header Table: \t" 
				+ Convertions.bytesToInt(shnum.data,
						0, shnum.bSize));
		System.out.println("|----ELF Header Size: \t\t\t\t\t" 
				+ Convertions.bytesToInt(ehsize.data,
						0, ehsize.bSize));
		System.out.println("|----Section Header Table Index <-> String Table:\t" +
						Convertions.bytesToInt(shstrndx.data, 0, shstrndx.bSize));
		System.out.println("|--ELF-Header");
	}

	@Override
	public byte[] getBytes() {
		BData bd[] = {iMagic0, iMagic1, iMagic2, iMagic3, iFileBitClass, iDataEncoding,
				iHVersion, iPadding, type, machine, fVersion, entry, phoff, shoff, flags,
				ehsize, phentsize, phnum, shentsize, shnum, shstrndx};
		byte[] bytes = new byte[size];
		
		int bp = 0;
		for (int i = 0; i < bd.length; i++){
			for (int j = 0; j < bd[i].bSize; j++){
				bytes[bp++] = bd[i].data[j];
			}
		}
		return bytes;
	}

	@Override
	public int getSize() {
		//check size from ehsize Attribute
		size = Convertions.bytesToInt(ehsize.data, 0, ehsize.bSize);
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
