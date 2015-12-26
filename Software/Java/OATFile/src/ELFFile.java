public class ELFFile {
	public ELFHeader header;
	public ELFProgramHeaderTable phtable;
	public ELFSymbolTable sytable;
	public ELFSectionHeaderStringTable shstrtable;
	public ELFSymbolHashTable htable;
	public OatdataSection oatdata;
	public OatexecSection oatexec;
	public ELFSectionHeaderTable shtable;
	
	public byte[] bytes;
	public int exe_off;
	public int exe_size;
	
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
	
		//find symbol/string/hash/exe sections
		int sytable_off = 0;
		int sytable_size = 0;
		int systrtable_off = 0;
		int systrtable_size = 0;
		int htable_off = 0;
		int htable_size = 0;
		int oatdata_size = 0;
		int oatdata_off = 0;
		for (int i = 0; i < shtable.entries.length; i++){
			if (shtable.entries[i].sName.equals(".dynsym")){
				sytable_off = Convertions.bytesToInt(
						shtable.entries[i].boffset.data,
						0,shtable.entries[i].boffset.bSize);
				sytable_size = Convertions.bytesToInt(
						shtable.entries[i].bsize.data,
						0,shtable.entries[i].bsize.bSize);
			}
			if(shtable.entries[i].sName.equals(".dynstr")){
				systrtable_off = Convertions.bytesToInt(
								shtable.entries[i].boffset.data,
								0,shtable.entries[i].boffset.bSize);
				systrtable_size = Convertions.bytesToInt(
						shtable.entries[i].bsize.data,
						0,shtable.entries[i].bsize.bSize);
			}
			if(shtable.entries[i].sName.equals(".hash")){
				htable_off = Convertions.bytesToInt(
								shtable.entries[i].boffset.data,
								0,shtable.entries[i].boffset.bSize);
				htable_size = Convertions.bytesToInt(
						shtable.entries[i].bsize.data,
						0,shtable.entries[i].bsize.bSize);
			}
			if(shtable.entries[i].sName.equals(".text")){
				exe_off = Convertions.bytesToInt(
								shtable.entries[i].boffset.data,
								0,shtable.entries[i].boffset.bSize);
				exe_size = Convertions.bytesToInt(
						shtable.entries[i].bsize.data,
						0,shtable.entries[i].bsize.bSize);
			}
			if(shtable.entries[i].sName.equals(".rodata")){
				oatdata_off = Convertions.bytesToInt(
								shtable.entries[i].boffset.data,
								0,shtable.entries[i].boffset.bSize);
				oatdata_size = Convertions.bytesToInt(
						shtable.entries[i].bsize.data,
						0,shtable.entries[i].bsize.bSize);
			}
			
		}
		sytable = new ELFSymbolTable(bytes, sytable_off,
				sytable_size, systrtable_off, systrtable_size);
		shstrtable = new ELFSectionHeaderStringTable(bytes, systrtable_off, systrtable_size);
		
		htable = new ELFSymbolHashTable(bytes, htable_off, htable_size);
		
		
		oatdata = new OatdataSection(bytes, oatdata_off, oatdata_size); // == .rodata
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
