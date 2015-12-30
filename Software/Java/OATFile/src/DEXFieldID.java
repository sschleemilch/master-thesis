
public class DEXFieldID extends Section{
	public int offset;
	public int size;
	
	
	public BData class_idx;
	public BData type_idx;
	public BData name_idx;
	
	public DEXFieldID(byte[] src, int off){
		class_idx = new BData(off + 0, new byte[]{src[off+0], src[off+1]});
		type_idx = new BData(off + 2, new byte[]{src[off+2], src[off+3]});
		name_idx = new BData(off + 4, new byte[]{src[off+4],
				src[off+5],src[off+6],src[off+7]});
		this.size = class_idx.bSize + type_idx.bSize + name_idx.bSize;
		this.offset = off;
	}
	
	@Override
	public void dump() {
		System.out.println("|------------FieldID:");
		System.out.println("|----------------Class idx:\t" + class_idx.getInt());
		System.out.println("|----------------Type idx:\t" + type_idx.getInt());
		System.out.println("|----------------Name idx:\t" + name_idx.getInt());
		System.out.println("|------------FieldID:");
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {class_idx, type_idx, name_idx};
		
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
