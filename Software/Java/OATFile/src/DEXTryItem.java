
public class DEXTryItem extends Section{
	public int offset;
	public int size;
	
	public BData start_addr;
	public BData insn_count;
	public BData handler_off;
	
	public DEXTryItem(byte[]src, int off){
		this.offset = off;
		start_addr = new BData(off, new byte[]{src[off + 0],
				src[off + 1], src[off + 2], src[off + 3]});
		insn_count = new BData(off + 4, new byte[]{src[off + 4],src[off + 5]});
		handler_off = new BData(off + 6, new byte[]{src[off + 6],src[off + 7]});
		
		this.size = start_addr.bSize + insn_count.bSize + handler_off.bSize;
	}
	
	
	@Override
	public void dump() {
		System.out.println("|------------Try Item");
		System.out.print("|----------------Start Addr:\t");
		System.out.printf("0x%08X\n", start_addr.getInt());
		System.out.println("|----------------Insn Count:\t" + insn_count.getInt());
		System.out.print("|----------------Handler Off:\t");
		System.out.printf("0x%08X\n", handler_off.getInt());
		System.out.println("|------------Try Item");
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {start_addr, insn_count, handler_off};
		
		byte[] b = new byte[size];
		int bp = 0;
		for (int i = 0; i < bd.length; i++){
			for(int j = 0; j < bd[i].bSize; j++){
				b[bp++] = bd[i].data[j];
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
