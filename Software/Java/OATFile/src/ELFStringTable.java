import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.Synthesizer;

public class ELFStringTable extends ELFSection{
	private byte[] stringTable;
	private int size;
	private int offset;
	
	public ELFStringTable(byte[]src, int off, int size){
		this.stringTable = Arrays.copyOfRange(src, off, off+size);
		this.size = size;
		this.offset = off;
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
		System.out.println("|");
		System.out.println("|--ELF String Table");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		String tmp = "-1";
		int index = 0;
		int lastlength = 0;
		while(tmp != ""){
			tmp = getString(index);
			lastlength = tmp.length();
			if (tmp != ""){
				System.out.println("|----Index:\t" + index + "\t" + tmp);
			}
			index+=lastlength+1;
		}
		System.out.println("|--ELF String Table");
	}
	@Override
	public byte[] getBytes() {
		return stringTable;
	}
	@Override
	public int getSize() {
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
