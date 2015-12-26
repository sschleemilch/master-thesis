
public class DEXMapList extends ELFSection{
	public BData bsize;
	public DEXMapItem[] list;
	
	private int size;
	private int offset;
	
	public DEXMapList(byte[] src, int off){
		bsize = new BData(off + 0 , new byte[]{src[off+0], src[off+1],
				src[off+2],src[off+3]});
		list = new DEXMapItem[Convertions.bytesToInt(bsize.data, 0, bsize.bSize)];
		size = bsize.bSize;
		for (int i = 0; i < list.length; i++){
			list[i] = new DEXMapItem(src, off + 4 + (i*12));
			size+=list[i].getSize();
		}
		offset = off;
	}
	public void dump(){
		System.out.println("\n\tDEX MAP LIST ------------------------->");
		System.out.println("\tSize: \t" + Convertions.bytesToInt(bsize.data, 0, bsize.bSize));
		for (int i = 0 ; i < list.length; i++){
			list[i].dump();
		}
		System.out.println("\tEND OF DEX MAP LIST ------------------<");
	}
	@Override
	public byte[] getBytes() {
		byte[] bytes = new byte[size];
		int bp = 0;
		
		for (int i = 0; i < bsize.bSize; i++){
			bytes[bp++] = bsize.data[i];
		}
		for(int i = 0; i < list.length; i++){
			byte[] tmp = list[i].getBytes();
			for (int j = 0; j < tmp.length; j++){
				bytes[bp++] = tmp[j];
			}
		}
		return bytes;
	}
	@Override
	public int getSize() {
		return size;
	}
	@Override
	public int getOffset() {
		return offset;
	}
}
