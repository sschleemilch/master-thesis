
public class DEXEncodedCatchHandler extends Section{
	public int offset;
	public int size;
	
	public BData list_size_sleb;
	public DEXEncodedTypeAddrPair[] handlers;
	public BData catch_all_addr_uleb;
	
	public DEXEncodedCatchHandler(byte[]src, int off) {
		this.offset = off;
		int doff = off;
		list_size_sleb = new BData(src, doff, "sleb128");
		doff += list_size_sleb.bSize;
		handlers = new DEXEncodedTypeAddrPair[Math.abs(list_size_sleb.getSleb128())];
		for(int i = 0; i < handlers.length; i++){
			handlers[i] = new DEXEncodedTypeAddrPair(src, doff);
			doff += handlers[i].getSize();
		}
		if(list_size_sleb.getSleb128() < 0){
			catch_all_addr_uleb = new BData(src, doff, "uleb128");
			doff += catch_all_addr_uleb.bSize;
		}
		this.size = doff - off;
	}
	
	
	@Override
	public void dump() {
		System.out.println("|------------Encoded Catch Handler");
		System.out.println("|----------------List Size:\t" + list_size_sleb.getSleb128());
		System.out.println("|----------------Enc Type Addr Pairs:");
		for ( int i = 0; i < handlers.length; i++){
			handlers[i].dump();
		}
		if (catch_all_addr_uleb != null){
			System.out.print("|----------------Catch All Addr:\t");
			System.out.printf("0x%08X\n", catch_all_addr_uleb.getUleb128());
		}
		System.out.println("|------------Encoded Catch Handler");
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
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
