import java.util.Arrays;

public class DEXData extends Section{

	public int offset;
	public int size;
	
	byte[] bytes;
	
	
	public DEXData(byte[] src, int off, int size){
		this.offset = off;
		this.size = size;
		bytes = Arrays.copyOfRange(src, off , off + size);
	}
	
	@Override
	public void dump() {
		System.out.println("|--------Data");
		System.out.print("|------------Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Size:\t");
		System.out.printf("0x%08X\n", size);
		System.out.println("|--------Data");
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	public void updateBytes(byte[] src){
		bytes = Arrays.copyOfRange(src, offset , offset + size);
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
