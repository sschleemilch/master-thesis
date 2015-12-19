import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ELFStringTable {
	private byte[] stringTable;
	public ELFStringTable(byte[]src, int offset, int size){
		this.stringTable = Arrays.copyOfRange(src, offset, offset+size);
	}
	public String getString(int index){
		List<Byte> l = new ArrayList<Byte>();
		if (index < stringTable.length){
			if(stringTable[index] == 0x00){
				index++;
			}
			while(stringTable[index]!=0x00){
				l.add(stringTable[index++]);
			}
			return byteListToString(l);
		}else{
			return "";
		}
	}
	private String byteListToString(List<Byte> l){
		if (l==null){
			return "";
		}	
		byte[]array = new byte[l.size()];
		int i = 0;
		for (Byte current : l){
			array[i] = current;
			i++;
		}
		return new String(array, StandardCharsets.UTF_8);
	}
}
