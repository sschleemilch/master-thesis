public class ELFFile {
	public ELFHeader header;
	public ELFSectionHeaderTable shtable;
	public ELFSymbolTable sytable;
	public byte[] bytes;
	public OatdataSection oatdata;
	public ELFProgramHeaderTable phtable;
	public ELFStringTable strtable;
	public ELFHashTable htable;
	public int exe_off;
	public int exe_size;
	
	public ELFFile(String pathToFile){
		//load ELF File
		bytes = FileOperations.readFileInBytes(pathToFile);
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
	
		//find symbol/string/hash/exe sections
		int sytable_off = 0;
		int sytable_size = 0;
		int systrtable_off = 0;
		int systrtable_size = 0;
		int htable_off = 0;
		int htable_size = 0;
		for (int i = 0; i < shtable.entries.length; i++){
			if (shtable.entries[i].sName.equals(".dynsym")){
				sytable_off = Convertions.bytesToInt(
						shtable.entries[i].offset.data,
						0,shtable.entries[i].offset.bSize);
				sytable_size = Convertions.bytesToInt(
						shtable.entries[i].size.data,
						0,shtable.entries[i].size.bSize);
			}
			if(shtable.entries[i].sName.equals(".dynstr")){
				systrtable_off = Convertions.bytesToInt(
								shtable.entries[i].offset.data,
								0,shtable.entries[i].offset.bSize);
				systrtable_size = Convertions.bytesToInt(
						shtable.entries[i].size.data,
						0,shtable.entries[i].size.bSize);
			}
			if(shtable.entries[i].sName.equals(".hash")){
				htable_off = Convertions.bytesToInt(
								shtable.entries[i].offset.data,
								0,shtable.entries[i].offset.bSize);
				htable_size = Convertions.bytesToInt(
						shtable.entries[i].size.data,
						0,shtable.entries[i].size.bSize);
			}
			if(shtable.entries[i].sName.equals(".text")){
				exe_off = Convertions.bytesToInt(
								shtable.entries[i].offset.data,
								0,shtable.entries[i].offset.bSize);
				exe_size = Convertions.bytesToInt(
						shtable.entries[i].size.data,
						0,shtable.entries[i].size.bSize);
			}
			
		}
		sytable = new ELFSymbolTable(bytes, sytable_off,
				sytable_size, systrtable_off, systrtable_size);
		strtable = new ELFStringTable(bytes, systrtable_off, systrtable_size);
		
		htable = new ELFHashTable(bytes, htable_off, htable_size);
		//hash table here...........
		
		int ti = -1; //tableindex
		for (int i = 0; i < sytable.entries.length; i++){
			if (sytable.entries[i].sName.equals("oatdata")&&
					Convertions.bytesToInt(sytable.entries[i].name.data,
							0, sytable.entries[i].name.bSize)>0){
				ti = Convertions.bytesToInt(sytable.entries[i].shndx.data,
						0, sytable.entries[i].shndx.bSize);
			}
		}
		int oatoff = Convertions.bytesToInt(shtable.entries[ti].offset.data,
				0, shtable.entries[ti].offset.bSize);
		oatdata = new OatdataSection(bytes, oatoff); // == .rodata
	}
	public byte[] getExecutable(){
		byte[] exe = new byte[exe_size];
		for (int i = 0; i < exe.length; i++){
			exe[i] = bytes[ exe_off + i];
		}
		return exe;
	}
	public void setNewExecutable(byte[] nexe){
		
	}
}
