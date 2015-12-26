
public class ELFNullSection extends ELFSection{
	private int offset;
	private int size;
	
	
	public ELFNullSection(int off, int size) {
		this.offset = off;
		this.size = size;
	}
	
	
	@Override
	public void dump() {
		System.out.println("|");
		System.out.println("|--ELF Null Section");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		System.out.println("|--ELF Null Section");
	}

	@Override
	public byte[] getBytes() {
		byte[] bytes = new byte[size];
		for (int i = 0; i < size; i++){
			bytes[i] = 0x00;
		}
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
	
	
}
