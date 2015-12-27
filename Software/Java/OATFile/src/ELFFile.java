import java.util.Arrays;

public class ELFFile {
	//File Sections
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
	public ELFSection[] sections = new ELFSection[12];
	
	//Raw byte content of ELF file
	public byte[] bytes;
	public int size;
	
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
		size = bytes.length;
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
		updateSize();
	}
	
	public int nZeroBytesBetweenSections(){
		int zeros = 0;
		for(int i = 0; i < sections.length - 1; i++){
			int sEnd = sections[i].getOffset() + sections[i].getSize();
			int sBegin = sections[i+1].getOffset();
			zeros += sBegin - sEnd;
		}
		return zeros;
	}
	
	//compute new size after changes
	public void updateSize(){
		int tsize = 0;
		for (int i = 0; i < sections.length; i++){
			tsize += sections[i].getSize();
		}
		tsize += nZeroBytesBetweenSections();
		size = tsize;
	}
	
	public byte[] getExecutable(){
		return oatexec.getBytes();
	}
	
	//get updated bytes if something has changed
	public byte[] getBytes(){
		updateSize();
		byte[] bytes = new byte[size];
		Arrays.fill(bytes, (byte)0);
		
		for(int i = 0; i < sections.length; i++){
			int soff = sections[i].getOffset();
			int ssize = sections[i].getSize();
			byte[] sb = sections[i].getBytes();
			for(int j = soff; j < soff + ssize; j++){
				bytes[j] = sb[j-soff];
			}
		}
		return bytes;
	}

	public void injectExecutable(byte[] exe){
		oatexec.setNewContent(exe); //setting new exe
		//adapting offsets of the following sections
		nsection2.setOffset(oatexec.getOffset()+oatexec.getSize());
		
		//keep alignment of 0x1000
		int nsecend = nsection2.getOffset() + nsection2.getSize();
		int aldif = 0;
		while (nsecend%4096 != 0){
			aldif++;
			nsecend++;
		}
		nsection2.setSize(nsection2.getSize() + aldif);
		dlinfo.setOffset(nsection2.getOffset() + nsection2.getSize());
		shstrtable.setOffset(dlinfo.getOffset() + dlinfo.getSize());
		shtable.setOffset(shstrtable.getOffset() + shstrtable.getSize());
		
		//updating size+offset depending sections
		header.shoff.setInt(shtable.getOffset());
		
		phtable.entries[2].filesz.setInt(oatexec.getSize());
		phtable.entries[2].memsz.setInt(oatexec.getSize());
		phtable.entries[3].boffset.setInt(dlinfo.getOffset());
		phtable.entries[3].vaddr.setInt(dlinfo.getOffset());
		phtable.entries[3].paddr.setInt(dlinfo.getOffset());
		phtable.entries[4].boffset.setInt(dlinfo.getOffset());
		phtable.entries[4].vaddr.setInt(dlinfo.getOffset());
		phtable.entries[4].paddr.setInt(dlinfo.getOffset());
		
		sytable.entries[2].bsize.setInt(oatexec.getSize());
		
		shtable.entries[5].bsize.setInt(oatexec.getSize());
		shtable.entries[6].boffset.setInt(dlinfo.getOffset());
		shtable.entries[7].boffset.setInt(shstrtable.getOffset());
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
