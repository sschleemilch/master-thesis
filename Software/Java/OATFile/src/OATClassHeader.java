
public class OATClassHeader extends ELFSection{
	public BData status;
	public BData type;
	public BData bitmap_size;
	public BData bitmap;
	public BData methods_offsets;
	
	private int size;
	private int offset;
	
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
		offset = off;
	}
	public void dump(){
		System.out.println("|--------Oat Class Header");
		System.out.print("|------------Offset:\t\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Status:\t\t");
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
			System.out.println("|------------Type:\t\t" + Convertions.bytesToInt(type.data, 0, type.bSize));
			System.out.println("|------------Bitmap Size:\t" + Convertions.bytesToInt(bitmap_size.data, 0, bitmap_size.bSize));
			System.out.println("|------------Bitmap:\t\t");
			for (int i = 0; i < bitmap.data.length; i++){
				System.out.printf("\t\t0x%02X\n",bitmap.data[i]);
			}

		}
		System.out.println("|--------Oat Class Header");
	}
	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
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
