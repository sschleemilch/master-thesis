
public class DEXProtoIDItem extends Section{

	public int offset;
	public int size;
	
	public BData shorty_idx; //entry index in string_ids which are ShortyDescriptors
	public BData return_type_idx; //They must correspond to return type and parameters
	public BData parameters_off;
	
	public DEXProtoIDItem(byte[]src, int off){
		this.offset = off;
		shorty_idx = new BData(off + 0, new byte[]{src[off+0],
				src[off+1],src[off+2],src[off+3]});
		return_type_idx = new BData(off + 4, new byte[]{src[off+4],
				src[off+5],src[off+6],src[off+7]});
		parameters_off = new BData(off + 8, new byte[]{src[off+8],
				src[off+9],src[off+10],src[off+11]});
		this.size = shorty_idx.bSize + return_type_idx.bSize + parameters_off.bSize;
		this.offset = off;
	}
	
	
	@Override
	public void dump() {
		System.out.println("|------------ProtoID:");
		System.out.println("|----------------Shorty idx:\t" + shorty_idx.getInt());
		System.out.println("|----------------Ret type idx:\t" + return_type_idx.getInt());
		System.out.print("|----------------Para off:\t");
		System.out.printf("0x%08X\n", parameters_off.getInt());
		System.out.println("|------------ProtoID:");
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {shorty_idx, return_type_idx, parameters_off};
		
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
