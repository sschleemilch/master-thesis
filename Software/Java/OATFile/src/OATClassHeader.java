import java.util.Arrays;

public class OATClassHeader extends Section{
	public BData status;
	public BData type;
	public BData bitmap_size;
	public BData bitmap;
	public BData methods_offsets;
	
	private int size;
	private int offset;
	
	private int mosize = 0;
	private int mooff;
	
	public OATClassHeader(byte[] src, int off, int size){
		status = new BData(off + 0, new byte[]{src[off+0],src[off+1]});
		type = new BData(off + 2, new byte[]{src[off+2],src[off+3]});
		if (type.data[0] == 1){
			bitmap_size = new BData(off + 4, new byte[]{src[off+4],src[off+5],
					src[off+6],src[off+7]});
			int bsize = bitmap_size.getInt();
			byte bdata[] = new byte[bsize];
			for (int i = 0; i < bsize; i++){
				bdata[i] = src[off+8+i];
			}
			bitmap = new BData(off+8, bdata);
			
			mosize = size - status.bSize - type.bSize - bitmap_size.bSize -
					bitmap.bSize;
			mooff = off + status.bSize + type.bSize + bitmap_size.bSize +
					bitmap.bSize;
		}else{
			bitmap_size = null;
			bitmap = null;
			mosize = size - status.bSize - type.bSize;
			mooff = off + status.bSize + type.bSize;
		}
		
		if(mosize >= 4){
			methods_offsets = new BData(mooff, Arrays.copyOfRange(src, mooff, mooff + mosize));
		}
		
		offset = off;
		this.size = size;
	}
	public void dump(){
		System.out.println("|--------Oat Class Header");
		System.out.print("|------------Offset:\t\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|------------Status:\t\t");
		switch(type.getInt()){
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
			System.out.println("|------------Type:\t\t" + type.getInt());
			System.out.println("|------------Bitmap Size:\t" + bitmap_size.getInt());
			System.out.println("|------------Bitmap:\t\t");
			for (int i = 0; i < bitmap.data.length; i++){
				System.out.printf("\t\t0x%02X\n",bitmap.data[i]);
			}

		}
		if(mosize>=4){
			System.out.println("|------------Method Offs:");
			for(int i = 0; i < mosize/4; i++){
				int v = Convertions.bytesToInt(Arrays.copyOfRange(methods_offsets.data, i*4, (i*4)+4), 0, 4);
				System.out.printf("|----------------0x%08X\n", v);
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
