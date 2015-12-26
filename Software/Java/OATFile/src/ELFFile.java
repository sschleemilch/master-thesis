public class ELFFile {
	//ELF Sections
	public ELFHeader header;
	public ELFProgramHeaderTable phtable;
	public ELFSymbolTable sytable;
	public ELFStringTable systrtable;
	public ELFSymbolHashTable syhtable;
	public ELFNullSection nsection1;
	public ELFOatdataSection oatdata;
	public ELFOatexecSection oatexec;
	public ELFNullSection nsection2;
	public ELFDynamicLinkingInfo dlinfo;
	public ELFStringTable shstrtable;
	public ELFSectionHeaderTable shtable;
	
	//Container for all Sections
	public int nsections = 12; 
	public ELFSection[] sections = new ELFSection[nsections];
	
	//Raw byte content of ELF file
	public byte[] bytes;
	
	//Sizes and offsets of Sections
	public int sytab_off;
	public int sytab_size;
	public int systrtab_off;
	public int systrtab_size;
	public int syhtab_off;
	public int syhtab_size;
	public int nsection1_off;
	public int nsection1_size;
	public int oatdata_off;
	public int oatdata_size;
	public int oatexec_off;
	public int oatexec_size;
	public int nsection2_off;
	public int nsection2_size;
	public int dylinfo_off;
	public int dylinfo_size;
	public int shstrtab_off;
	public int shstrtab_size;
	
	public ELFFile(String pathToFile){
		//load ELF File
		bytes = FileOperations.readFileToBytes(pathToFile);
		header = new ELFHeader(bytes);
		phtable = new ELFProgramHeaderTable(bytes,
				Convertions.bytesToInt(header.phoff.data, 0, header.phoff.bSize),
				Convertions.bytesToInt(header.phnum.data, 0, header.phnum.bSize),
				Convertions.bytesToInt(header.phentsize.data, 0, header.phentsize.bSize));
		shtable = new ELFSectionHeaderTable(bytes,
				Convertions.bytesToInt(header.shoff.data, 0, header.shoff.bSize),
				Convertions.bytesToInt(header.shnum.data, 0, header.shnum.bSize),
				Convertions.bytesToInt(header.shentsize.data, 0, header.shentsize.bSize),
				Convertions.bytesToInt(header.shstrndx.data, 0, header.shstrndx.bSize));
		fillSectionsInfo();
		sytable = new ELFSymbolTable(bytes, sytab_off, sytab_size, systrtab_off, systrtab_size);
		systrtable = new ELFStringTable(bytes, systrtab_off, systrtab_size);
		syhtable = new ELFSymbolHashTable(bytes, syhtab_off, syhtab_size);
		nsection1 = new ELFNullSection(nsection1_off, nsection1_size);
		oatdata = new ELFOatdataSection(bytes, oatdata_off, oatdata_size);
		oatexec = new ELFOatexecSection(bytes, oatexec_off, oatexec_size);
		nsection2 = new ELFNullSection(nsection2_off, nsection2_size);
		dlinfo = new ELFDynamicLinkingInfo(bytes, dylinfo_off, dylinfo_size);
		shstrtable = new ELFStringTable(bytes, shstrtab_off, shstrtab_size);
		fillSectionsContainer();
		
		
	}

	public void fillSectionsContainer(){
		sections[0] = header;
		sections[1] = phtable;
		sections[2] = sytable;
		sections[3] = systrtable;
		sections[4] = syhtable;
		sections[5] = nsection1;
		sections[6] = oatdata;
		sections[7] = oatexec;
		sections[8] = nsection2;
		sections[9] = dlinfo;
		sections[10] = shstrtable;
		sections[11] = shtable;
	}
	
	public void fillSectionsInfo(){
		for (int i = 0; i < shtable.entries.length; i++){
			
			int of = Convertions.bytesToInt(shtable.entries[i].boffset.data,
					0, shtable.entries[i].boffset.bSize);
			int si = Convertions.bytesToInt(shtable.entries[i].bsize.data,
					0, shtable.entries[i].bsize.bSize);
			
			switch(shtable.entries[i].sName){
			case ".dynsym":
				sytab_off = of;
				sytab_size = si;
				break;
			case ".dynstr":
				systrtab_off = of;
				systrtab_size = si;
				break;
			case ".hash":
				syhtab_off = of;
				syhtab_size = si;
				break;
			case ".rodata":
				oatdata_off = of;
				oatdata_size = si;
				break;
			case ".text":
				oatexec_off = of;
				oatexec_size = si;
				break;
			case ".dynamic":
				dylinfo_off = of;
				dylinfo_size = si;
				break;
			case ".shstrtab":
				shstrtab_off = of;
				shstrtab_size = si;
				break;
			}
		}
		nsection1_off = syhtab_off + syhtab_size;
		nsection1_size = oatdata_off - nsection1_off;
		
		nsection2_off = oatexec_off + oatexec_size;
		nsection2_size = dylinfo_off - nsection2_off;
	}
	
	public void dump(){
		System.out.println("\n|>>> ELF File Format <<<");
		System.out.println("|");
		for (int i = 0; i < sections.length; i++){
			sections[i].dump();
		}
		System.out.println("|");
		System.out.println("-");
	}
}
