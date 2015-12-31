
public class DEXEncodedMethod extends Section{
	public BData method_idx_diff_uleb;
	public BData access_flags_uleb;
	public BData code_off_uleb;
	
	public int offset;
	public int size;
	
	public DEXEncodedMethod(byte[]src, int off){
		int doff = off;
		this.offset = off;
		
		method_idx_diff_uleb = new BData(src, doff, "uleb128");
		doff += method_idx_diff_uleb.bSize;
		access_flags_uleb = new BData(src, doff, "uleb128");
		doff += access_flags_uleb.bSize;
		code_off_uleb = new BData(src, doff, "uleb128");
		
		size = method_idx_diff_uleb.bSize + access_flags_uleb.bSize +
				code_off_uleb.bSize;
	}

	@Override
	public void dump() {
		System.out.println("|----------------DexEncoded Method");
		System.out.println("|--------------------Method idx diff:\t" 
				+ method_idx_diff_uleb.getUleb128());
		System.out.println("|--------------------Access Flags:\t" + 
				access_flags_uleb.getUleb128());
		System.out.print("|--------------------Code Off:\t\t");
		System.out.printf("0x%08X\n",code_off_uleb.getUleb128());
		System.out.println("|----------------DexEncoded Method");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[size];
		int bp = 0;
		BData[] d = {method_idx_diff_uleb, access_flags_uleb, 
				code_off_uleb};
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
