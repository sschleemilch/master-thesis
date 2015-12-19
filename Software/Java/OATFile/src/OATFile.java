import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class OATFile {
	
	public byte [] rawBytes;
	//Offsets, absolute...
	public int oatFile_offset;
	public int oatFile_executable_offset;
	public int emb_dexFile_offset;
	public int oatFile_dexFile_header_offset;
	//--------------------
	public String oatFile_version;
	public int oatFile_header_crc;
	public String oatFile_instruction_set;
	public int oatFile_n_embedded_dex_files;
	private int oatFile_kvs_size;
	public String[] oatFile_keyValuePairs;
	private int src_dexFile_path_size;
	public String src_dexFile_path;
	public int src_dexFile_crc;
	public int emb_dexFile_size;
	public int[] oatFile_oat_class_headers;
	
	public OATFile(String filePath){
		Path path = Paths.get(filePath);
		try {
			rawBytes = Files.readAllBytes(path);
		}catch (IOException e){
			System.exit(0);
			e.getMessage();
		}
		
		oatFile_offset = getOatFileOffset();
		int p = oatFile_offset;
		//OAT HEADER---------------------------
		p+=4; //skip magic
		oatFile_version = new String(Arrays.copyOfRange(rawBytes,
				p, p+3));
		p+=4;//skip version
		oatFile_header_crc = Convertions.bytesToInt(rawBytes, p, 4);
		p+=4;//skip crc
		oatFile_instruction_set = getOatFileInstructionSet(p);
		p+=4;//skip instruction set
		p+=4; //skip instruction set features
		oatFile_n_embedded_dex_files = Convertions.bytesToInt(rawBytes,p, 4);
		p+=4;//skip dex_file_count
		oatFile_executable_offset = oatFile_offset +
				Convertions.bytesToInt(rawBytes,p, 4);
		p+=4;//skip executable offset
		p+=4;//skip interpreter to interpreter bridge offset
		p+=4;//skip interpreter to comiled code bridge offset
		p+=4;//skip jni dlsym lookup offset
		p+=4;//skip portable imt conflict trampoline offset
		p+=4;//skip portable resolution trampline offset
		p+=4;//skip portable to interpreter bridge offset
		p+=4;//skip quick generic jni trampoline offset
		p+=4;//skip quick imt conflict trampoline offset
		p+=4;//skip quick resolution trampoline offset
		p+=4;//skip quick to interpreter bridge offset
		p+=4;//skip image patch delta
		p+=4;//skip image file location oat checksum
		p+=4;//skip image file location oat data begin
		
		oatFile_kvs_size = Convertions.bytesToInt(rawBytes,p, 4);
		p+=4; //skip kvs size
		oatFile_keyValuePairs = extractKeyPairStrings(p, oatFile_kvs_size);
		p+=oatFile_kvs_size;
		
		//OAT DEX Header-----------------------------
		oatFile_dexFile_header_offset = p;
		src_dexFile_path_size = Convertions.bytesToInt(rawBytes,p, 4);
		p+=4; //skip dex src path size
		src_dexFile_path = new String(Arrays.copyOfRange(rawBytes,
				p, p + src_dexFile_path_size));
		p+=src_dexFile_path_size;
		src_dexFile_crc = Convertions.bytesToInt(rawBytes,p, 4);
		p+=4; //skip dex source crc
		emb_dexFile_offset = oatFile_offset + Convertions.bytesToInt(rawBytes,p, 4);
		emb_dexFile_size = Convertions.bytesToInt(rawBytes,emb_dexFile_offset + 0x20, 4);
		p+=4; //skip embedded dex file offset
		 
		//oatclass header offsets
		byte[] offsetList = Arrays.copyOfRange(rawBytes, p, emb_dexFile_offset);
		oatFile_oat_class_headers = fillOatClassHeaderOffsets(offsetList);
		System.out.println("DONE");
	}
	

	
	private int getOatFileOffset(){
		for (int i = 0; i < this.rawBytes.length; i++){
			if (rawBytes[i] == 0x6f && i + 3 < rawBytes.length ){
				if (rawBytes[i+1] == 0x61 &&
					rawBytes[i+2] == 0x74 &&
					rawBytes[i+3] == 0x0A){
					return i;
				}
			}		
		}
		return -1;
	}
	
	private int[] fillOatClassHeaderOffsets(byte[]offs){
		int [] result = new int[offs.length/4];
		int c = 0;
		for (int i = 0; i < offs.length; i+=4){
			if (i+3 < offs.length){
				byte[] tmp = Arrays.copyOfRange(offs, i, i+4);
				result[c++] = java.nio.ByteBuffer.wrap(tmp).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
			}
		}
		return result;
	}
	
	private String getOatFileInstructionSet(int offset){
		int instructionSetNumber = Convertions.bytesToInt(rawBytes,offset, 4);
		switch(instructionSetNumber){
		case 0:
			return "Unspecified";
		case 1:
			return "ARM";
		case 2:
			return "ARM_64";
		case 3:
			return "Thumb-2";
		case 4:
			return "X86";
		case 5:
			return "X64";
		case 6:
			return "MIPS";
		case 7:
			return "MIPS_64";
		}
		return "";
	}
	private String[] extractKeyPairStrings(int offset, int nBytes){
		byte[] kvs = Arrays.copyOfRange(rawBytes, offset, offset + nBytes);
		ArrayList<String> list = new ArrayList<String>();
		int border = 0;
		boolean emergencyBreak = false;
		for (int i = 0; i < kvs.length; i++){
			int c = 0;
			while(kvs[i] != 0x00){
				c++;
				i++;
				if (i > kvs.length){
					emergencyBreak = true;
					break;
				}
			}
			if (emergencyBreak){
				break;
			}
			list.add(new String(Arrays.copyOfRange(kvs,
					border, border+c)));
			border +=(c+1);
			c=0;
		}
		String[] ret = new String[list.size()];
		ret = list.toArray(ret);
		return ret;
	}
}
