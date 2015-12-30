import java.util.Arrays;

public class DEXTypeIDs extends Section{

	public int offset;
	public int size;
	
	public BData[] descriptor_idx;
	
	public DEXTypeIDs(byte[] src, int off, int size) {
		this.size = size*4;
		this.offset = off;
		descriptor_idx = new BData[size];
		for(int i = 0; i < size; i++){
			descriptor_idx[i] = new BData(off + i*4,
					Arrays.copyOfRange(src, off + i*4, off + (i*4) + 4));
		}
	}
	
	@Override
	public void dump() {
		System.out.println("|------------Type IDs");
		System.out.print("|------------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Size:\t");
		System.out.printf("0x%08X\n", size);
		for (int i = 0; i < descriptor_idx.length; i++){
			System.out.print("|----------------");
			System.out.printf("0x%08X\n", descriptor_idx[i].getInt());
		}
		System.out.println("|------------Type IDs");
	}

	@Override
	public byte[] getBytes() {
		byte [] b = new byte[size];
		int bp = 0;
		for (int i = 0; i < size; i++){
			byte[] tmp = descriptor_idx[i].data;
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
