import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ELFFile {
	public ELFHeader header;
	public ELFSectionHeaderTable shtable;
	public ELFSymbolTable sytable;
	public byte[] rawBytes;
	
	public ELFFile(String pathToFile){
		//load ELF File
		Path path = Paths.get(pathToFile);
		try {
			rawBytes = Files.readAllBytes(path);
		}catch (IOException e){
			System.err.println("Could not load ELF-File. Exiting");
			System.exit(0);
			e.getMessage();
		}
		header = new ELFHeader(rawBytes);
		shtable = new ELFSectionHeaderTable(rawBytes,
				Convertions.bytesToInt(header.shoff.data, 0, header.shoff.bSize),
				Convertions.bytesToInt(header.shnum.data, 0, header.shnum.bSize),
				Convertions.bytesToInt(header.shentsize.data, 0, header.shentsize.bSize),
				Convertions.bytesToInt(header.shstrndx.data, 0, header.shstrndx.bSize));
		
		//find symbol table section
		int sytable_off = 0;
		int sytable_size = 0;
		int systrtable_off = 0;
		int systrtable_size = 0;
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
		}
		sytable = new ELFSymbolTable(rawBytes, sytable_off,
				sytable_size, systrtable_off, systrtable_size);
		
	}
	
	
	public void setOatFile(byte[] bytes){
		
	}
	
}
