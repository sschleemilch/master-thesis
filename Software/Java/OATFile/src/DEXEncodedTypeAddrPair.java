
public class DEXEncodedTypeAddrPair extends Section{
	public int offset;
	public int size;
	
	public BData type_idx_uleb;
	public BData addr_uleb;
	
	public DEXEncodedTypeAddrPair(byte[]src, int off) {
		this.offset = off;
		int doff = off;
		type_idx_uleb = new BData(src, doff, "uleb128");
		doff += type_idx_uleb.bSize;
		addr_uleb = new BData(src, doff, "uleb128");
		
		this.size = type_idx_uleb.bSize + addr_uleb.bSize;
	}
	
	@Override
	public void dump() {
		System.out.println("|--------------------Encoded Type Addr Pair");
		System.out.println("|------------------------Type Idx:\t" + 
		type_idx_uleb.getUleb128());
		System.out.print("|--------------------Address:\t");
		System.out.printf("0x%08X\n", addr_uleb.getUleb128());
		System.out.println("|--------------------Encoded Type Addr Pair");
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {type_idx_uleb, addr_uleb};
		
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
