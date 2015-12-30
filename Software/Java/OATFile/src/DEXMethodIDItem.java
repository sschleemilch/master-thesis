
public class DEXMethodIDItem extends Section{
	public int offset;
	public int size;
	
	public BData class_idx;
	public BData proto_idx;
	public BData name_idx;
	
	public DEXMethodIDItem(byte[] src, int off){
		class_idx = new BData(off + 0, new byte[]{src[off+0],src[off+1]});
		proto_idx = new BData(off + 2, new byte[]{src[off+2],src[off+3]});
		name_idx = new BData(off + 8, new byte[]{src[off+8],
				src[off+9],src[off+10],src[off+11]});
		this.size = class_idx.bSize + proto_idx.bSize + name_idx.bSize;
		this.offset = off;
	}
	
	@Override
	public void dump() {
		System.out.println("|------------MethodID:");
		System.out.println("|----------------Class idx:\t" + class_idx.getInt());
		System.out.println("|----------------Proto idx:\t" + proto_idx.getInt());
		System.out.println("|----------------Name idx:\t" + name_idx.getInt());
		System.out.println("|------------MethodID:");
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {class_idx, proto_idx, name_idx};
		
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
