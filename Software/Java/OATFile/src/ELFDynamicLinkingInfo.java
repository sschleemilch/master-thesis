import java.util.Arrays;

public class ELFDynamicLinkingInfo extends ELFSection{

	private int size;
	private int offset;
	
	public byte[] bytes;
	
	public ELFDynamicLinkingInfo(byte[] src, int off, int size) {
		this.size = size;
		offset = off;
		bytes = Arrays.copyOfRange(src, off, off + size);
	}
	
	@Override
	public void dump() {
		System.out.println("|");
		System.out.println("|--ELF Dynamic Linking Info");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		System.out.println("|--ELF Dynamic Linking Info");
	}

	@Override
	public byte[] getBytes() {
		return bytes;
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
