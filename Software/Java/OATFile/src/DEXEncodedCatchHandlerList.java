
public class DEXEncodedCatchHandlerList extends Section{
	public int offset;
	public int size;
	
	public BData list_size_uleb;
	public DEXEncodedCatchHandler[] list;
	
	public DEXEncodedCatchHandlerList(byte[]src, int off) {
		this.offset = off;
		int doff = off;
		list_size_uleb = new BData(src, doff, "uleb128");
		doff += list_size_uleb.bSize;
		list = new DEXEncodedCatchHandler[list_size_uleb.getUleb128()];
		
		for(int i = 0; i < list.length; i++){
			list[i] = new DEXEncodedCatchHandler(src, doff);
			doff += list[i].getSize();
		}
		this.size = doff - off;
	}
	
	@Override
	public void dump() {
		System.out.println("|--------Encoded Catch Handler List");
		System.out.println("|------------List Size:\t" + list_size_uleb.getUleb128());
		System.out.println("|------------List");
		for (int i = 0; i < list.length; i++){
			list[i].dump();
		}
		System.out.println("|--------Encoded Catch Handler List");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[size];
		int bp = 0;
		
		byte[] tmp = list_size_uleb.data;
		for ( int i = 0; i < tmp.length; i++){
			b[bp++] = tmp[i];
		}
		for (int i = 0; i < list.length; i++){
			tmp = list[i].getBytes();
			for (int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
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
