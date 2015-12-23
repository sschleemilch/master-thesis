
public class DEXMapList {
	public BData size;
	public DEXMapItem[] list;
	
	public DEXMapList(byte[] src, int off){
		size = new BData(off + 0 , new byte[]{src[off+0], src[off+1],
				src[off+2],src[off+3]});
		list = new DEXMapItem[Convertions.bytesToInt(size.data, 0, size.bSize)];
		for (int i = 0; i < list.length; i++){
			list[i] = new DEXMapItem(src, off + 4 + (i*12));
		}
	}
	public void dump(){
		System.out.println("\n\tDEX MAP LIST ------------------------->");
		System.out.println("\tSize: \t" + Convertions.bytesToInt(size.data, 0, size.bSize));
		for (int i = 0 ; i < list.length; i++){
			list[i].dump();
		}
		System.out.println("\tEND OF DEX MAP LIST ------------------<");
	}
}
