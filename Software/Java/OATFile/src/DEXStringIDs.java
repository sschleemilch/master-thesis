import java.util.Arrays;

public class DEXStringIDs extends Section{
	
	public int offset;
	public int size;
	
	public DEXStringIDItem[] string_id_items;

	public DEXStringIDs(byte[] src, int off, int size){
		this.offset = off;
		string_id_items = new DEXStringIDItem[size];
		this.size = size * 4;
		for(int i = 0; i < string_id_items.length; i++){
			string_id_items[i] = new DEXStringIDItem(src, off + i*4);
		}
	}

	@Override
	public void dump() {
		System.out.println("|--------String IDs");
		System.out.print("|------------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Size:\t");
		System.out.printf("0x%08X\n", size);
		for(int i = 0; i < string_id_items.length; i++){
			string_id_items[i].dump();
		}
		System.out.println("|--------String IDs");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[string_id_items.length * size];
		int bp = 0;
		for (int i = 0; i < string_id_items.length; i++){
			byte[] t = string_id_items[i].getBytes();
			for (int j = 0; j < t.length; j++){
				b[bp++] = t[j];
			}
		}
		return b;
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
		this.offset = off;
	}
}
