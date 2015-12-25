
public class OATClassHeader {
	public BData status;
	public BData type;
	public BData bitmap_size;
	public BData bitmap;
	public BData methods_offsets;
	
	public OATClassHeader(byte[] src, int off){
		status = new BData(off + 0, new byte[]{src[off+0],src[off+1]});
		type = new BData(off + 2, new byte[]{src[off+2],src[off+3]});
		if (Convertions.bytesToInt(type.data, 0, type.bSize) == 1){
			bitmap_size = new BData(off + 4, new byte[]{src[off+4],src[off+5],
					src[off+6],src[off+7]});
			int bsize = Convertions.bytesToInt(bitmap_size.data, 0, bitmap_size.bSize);
			byte bdata[] = new byte[bsize];
			for (int i = 0; i < bsize; i++){
				bdata[i] = src[off+8+i];
			}
			bitmap = new BData(off+8, bdata);
		}else{
			bitmap_size = null;
			bitmap = null;
		}
	}
	public void dump(){
		System.out.println("OATClass HEADER ------------------>");
		System.out.print("Status:\t\t");
		switch(Convertions.bytesToInt(type.data, 0, type.bSize)){
		case 0:
			System.out.println("All methods compiled");
			break;
		case 1:
			System.out.println("Some methods compiled");
			break;
		case 2:
			System.out.println("No methods compiled");
			break;
		}
		if (bitmap_size != null){
			System.out.println("Type:\t\t" + Convertions.bytesToInt(type.data, 0, type.bSize));
			System.out.println("Bitmap Size:\t" + Convertions.bytesToInt(bitmap_size.data, 0, bitmap_size.bSize));
			System.out.println("Bitmap:\t\t");
			for (int i = 0; i < bitmap.data.length; i++){
				if(i%10==0){
					System.out.println("");
				}
				System.out.printf("0x%02X",bitmap.data[i]);
			}
			
		}
		System.out.println("END OF OATClass HEADER -----------<");
	}
}
