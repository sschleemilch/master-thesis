
public class OatexecSection extends ELFSection{
	public byte[] bytes;
	public int size;
	public int offset;
	
	public OatexecSection(byte[] src, int off, int size){
		bytes = new byte[size];
		this.size = size;
		for (int i = 0; i < bytes.length; i++){
			bytes[i] = src[off + i];
		}
		
	}

	public void setNewContent(byte[] nbytes){
		bytes = nbytes;
		size = bytes.length;
	}
	
	@Override
	public void dump() {
		//nothing to dump
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
