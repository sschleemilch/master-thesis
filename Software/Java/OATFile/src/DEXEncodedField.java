
public class DEXEncodedField extends Section{
	public BData field_idx_diff_uleb;
	public BData access_flags_uleb;
	
	public int size;
	public int offset;
	
	public DEXEncodedField(byte[]src, int off){
		this.offset = off;
		int doff = off;
		field_idx_diff_uleb = new BData(src, doff, "uleb128");
		doff += field_idx_diff_uleb.bSize;
		access_flags_uleb = new BData(src, doff, "uleb128");
		this.size = field_idx_diff_uleb.bSize + access_flags_uleb.bSize;
	}

	@Override
	public void dump() {
		System.out.println("|----------------DexEncoded Field");
		System.out.println("|--------------------Field idx diff:\t" 
				+ field_idx_diff_uleb.getUleb128());
		System.out.println("|--------------------Access Flags:\t" + 
				access_flags_uleb.getUleb128());
		System.out.println("|----------------DexEncoded Field");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[size];
		int bp = 0;
		BData[] d = {field_idx_diff_uleb, access_flags_uleb};
		for(int i = 0; i < d.length; i++){
			byte[] t = d[i].data;
			for (int j = 0; j < t.length; j++){
				b[bp++] = t[j];
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
