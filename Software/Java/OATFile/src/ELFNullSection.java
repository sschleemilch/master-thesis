
public class ELFNullSection extends ELFSection{
	public int offset;
	public int size;
	
	
	public ELFNullSection(int off, int size) {
		this.offset = off;
		this.size = size;
	}
	
	
	@Override
	public void dump() {
		//nothing to dump
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
