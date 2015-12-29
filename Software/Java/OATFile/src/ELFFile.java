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
	public Section[] sections = new Section[12];
	
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
				header.phoff.getInt(),
				header.phnum.getInt(),
				header.phentsize.getInt());
		shtable = new ELFSectionHeaderTable(bytes,
				header.shoff.getInt(),
				header.shnum.getInt(),
				header.shentsize.getInt(),
				header.shstrndx.getInt());
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
		byte[] by = new byte[size];
		Arrays.fill(by, (byte)0);
		
		for(int i = 0; i < sections.length; i++){
			int soff = sections[i].getOffset();
			int ssize = sections[i].getSize();
			byte[] sb = sections[i].getBytes();
			for(int j = soff; j < soff + ssize; j++){
				by[j] = sb[j-soff];
			}
		}
		return by;
	}
	
	public void fakeDexCRC(BData crc){
		oatdata.dex_file_location_checksum = crc;
		oatdata.bytes = new byte[oatdata.getSize()];
		int bp=0;
		for(int i = oatdata.getOffset(); i < oatdata.getOffset() + oatdata.getSize(); i++){
			oatdata.bytes[bp++] = bytes[i];
		}
	}
	public void fakeDexPath(BData path){
		oatdata.dex_file_location_data = path;
		oatdata.bytes = new byte[oatdata.getSize()];
		int bp=0;
		for(int i = oatdata.getOffset(); i < oatdata.getOffset() + oatdata.getSize(); i++){
			oatdata.bytes[bp++] = bytes[i];
		}
	}

	//assuming dex path lengh is the same
	public void deleteDex(byte[] crc, byte[] path){
		oatdata.dex_file_location_data.data = path;
		oatdata.dex_file_location_checksum.data = crc;
		oatdata.bytes = new byte[oatdata.getSize()];
		
		for(int i = 0; i < oatdata.getSize(); i++){
			oatdata.bytes[i] = bytes[oatdata.getOffset()+i];
		}
		//delete dex content
		int dexoff = Convertions.bytesToInt(oatdata.dex_file_pointer.data, 0, oatdata.dex_file_pointer.bSize);
		int dexend = oatdata.oat_class_headers[0].getOffset() - oatdata.getOffset();
		for(int i = dexoff; i < dexend; i++){
			oatdata.bytes[i] = 0x00;
		}
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
		updateSize();
	}
	
	public void fillSectionsInfo(){
		for (int i = 0; i < shtable.entries.length; i++){
			
			int of = shtable.entries[i].boffset.getInt();
			int si = shtable.entries[i].bsize.getInt();
			
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
