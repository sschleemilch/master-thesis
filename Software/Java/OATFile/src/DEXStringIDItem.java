
public class DEXStringIDItem extends Section{

	public int offset;
	public int size;
	
	public BData string_data_off;
	
	public DEXStringIDItem(byte[]src, int off){
		this.offset = off;
		string_data_off = new BData(off, new byte[]{src[off + 0],src[ off + 1],src[off + 2],src[off + 3]});
		this.size = string_data_off.bSize;
	}
	
	
	@Override
	public void dump() {
		System.out.println("|------------String ID Item");
		System.out.print("|----------------String Data off:\t");
		System.out.printf("0x%08X\n", string_data_off.getInt());
		System.out.println("|------------String ID Item");
	}

	@Override
	public byte[] getBytes() {
		return string_data_off.data;
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
