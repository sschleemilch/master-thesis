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
		if (index < stringTable.length - 1){
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
	public void dump(){
		System.out.println("ELF STRING TABLE------------>");
		String tmp = "-1";
		int index = 0;
		int lastlength = 0;
		while(tmp != ""){
			tmp = getString(index);
			lastlength = tmp.length();
			if (tmp != ""){
				System.out.println("Index:\t" + index + "\t" + tmp);
			}
			index+=lastlength+1;
		}
		System.out.println("ENF OF STRING TABLE --------<");
	}
}
