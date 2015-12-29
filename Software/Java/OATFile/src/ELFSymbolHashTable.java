
public class ELFSymbolHashTable extends Section{
	public BData nbucket;
	public BData nchain;
	public BData[] bucket;
	public BData[] chain;
	
	private int size;
	private int offset;
	
	public ELFSymbolHashTable(byte[] src, int off, int size){
		this.size = size;
		offset = off;
		nbucket = new BData(off + 0, new byte[]{src[off+0],
				src[off+1],src[off+2],src[off+3]});
		nchain = new BData(off + 4, new byte[]{src[off+4],
				src[off+5],src[off+6],src[off+7]});
		int doff = off + 8;
		bucket = new BData[nbucket.getInt()];
		chain = new BData[nchain.getInt()];
		
		for (int i = 0; i < bucket.length; i++){
			bucket[i] = new BData(doff + (i*4), new byte[]{src[doff + (i*4)+0],
					src[doff + (i*4)+1],src[doff + (i*4)+2],src[doff + (i*4)+3]});
		}
		doff += (bucket.length * 4);
		for (int i = 0; i < chain.length; i++){
			chain[i] = new BData(doff + (i*4), new byte[]{src[doff + (i*4)+0],
					src[doff + (i*4)+1],src[doff + (i*4)+2],src[doff + (i*4)+3]});
		}
		
	}
	public void dump(){
		System.out.println("|");
		System.out.println("|--ELF Symbol Hash Table");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		System.out.println("|----Bucket:");
		for (int i = 0; i < bucket.length; i++){
			System.out.println("|--------" + bucket[i].getInt());
		}
		System.out.println("|----Chain:");
		for (int i = 0; i < chain.length; i++){
			System.out.println("|--------" + chain[i].getInt());
		}
		System.out.println("|--ELF Symbol Hash Table");
	}
	@Override
	public byte[] getBytes() {
		BData[] bd = new BData[2 + bucket.length + chain.length];
		bd[0] = nbucket;
		bd[1] = nchain;
		
		int bdp = 2;
		for (int i = 0; i < bucket.length; i++){
			bd[bdp++] = bucket[i];
		}
		for (int i = 0; i < chain.length; i++){
			bd[bdp++] = chain[i];
		}
		
		byte[]bytes = new byte[size];
		
		int bp = 0;
		for (int i = 0; i < bd.length; i++){
			for (int j = 0; j < bd[i].bSize; j++){
				bytes[bp++] = bd[i].data[j];
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
	@Override
	public void setOffset(int off) {
		offset = off;	
	}
}
