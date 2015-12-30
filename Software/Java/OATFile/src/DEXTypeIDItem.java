
public class DEXTypeIDItem extends Section{

	public int offset;
	public int size;
	
	public BData descriptor_idx;
	
	public DEXTypeIDItem(byte[]src, int off) {
		this.offset = off;
		descriptor_idx = new BData(off, new byte[]{src[off + 0],src[off + 1],src[off + 2],src[off + 3]});
		this.size = descriptor_idx.bSize;
	}
	
	@Override
	public void dump() {
		System.out.println("|------------Type ID Item");
		System.out.println("|----------------Descr Idx:\t" + descriptor_idx.getInt());
		System.out.println("|------------String ID Item");
	}

	@Override
	public byte[] getBytes() {
		return descriptor_idx.data;
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
