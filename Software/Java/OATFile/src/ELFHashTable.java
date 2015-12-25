
public class ELFHashTable {
	public BData nbucket;
	public BData nchain;
	public BData[] bucket;
	public BData[] chain;
	public int size;
	
	public ELFHashTable(byte[] src, int off, int size){
		this.size = size;
		nbucket = new BData(off + 0, new byte[]{src[off+0],
				src[off+1],src[off+2],src[off+3]});
		nchain = new BData(off + 4, new byte[]{src[off+4],
				src[off+5],src[off+6],src[off+7]});
		int doff = off + 8;
		bucket = new BData[Convertions.bytesToInt(nbucket.data,
				0, nbucket.bSize)];
		chain = new BData[Convertions.bytesToInt(nchain.data,
				0, nchain.bSize)];
		
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
		System.out.println("ELF HASH TABLE -------------------->");
		System.out.println("Bucket:");
		for (int i = 0; i < bucket.length; i++){
			System.out.println("\t\t" + Convertions.bytesToInt(bucket[i].data, 0, bucket[i].bSize));
		}
		System.out.println("Chain:");
		for (int i = 0; i < chain.length; i++){
			System.out.println("\t\t" + Convertions.bytesToInt(chain[i].data, 0, chain[i].bSize));
		}
		System.out.println("END OF ELF HASH TABLE -------------<");
	}
}
