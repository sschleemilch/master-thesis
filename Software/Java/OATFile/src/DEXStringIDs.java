import java.util.Arrays;

public class DEXStringIDs extends Section{
	
	public int offset;
	public int size;
	
	public BData[] string_data_off;
	
	
	public DEXStringIDs(byte[] src, int off, int size){
		this.offset = off;
		this.size = size*4; //cause size means string offsets
		string_data_off = new BData[size];
		for(int i = 0; i < size; i++){
			string_data_off[i] = new BData(off + i*4,
					Arrays.copyOfRange(src, off + i*4, off + (i*4) + 4));
		}
	}



	@Override
	public void dump() {
		System.out.println("|--------String IDs");
		System.out.print("|------------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Size:\t");
		System.out.printf("0x%08X\n", size);
		for (int i = 0; i < string_data_off.length; i++){
			System.out.print("|------------");
			System.out.printf("0x%08X\n", string_data_off[i].getInt());
		}
		System.out.println("|--------String IDs");
		
	}

	@Override
	public byte[] getBytes() {
		byte [] b = new byte[size];
		int bp = 0;
		for (int i = 0; i < size; i++){
			byte[] tmp = string_data_off[i].data;
			for (int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
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
