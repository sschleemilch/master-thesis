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
		// TODO Auto-generated method stub
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
	
}
