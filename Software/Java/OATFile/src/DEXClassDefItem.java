
public class DEXClassDefItem extends Section{
	public int offset;
	public int size;

	public BData class_idx;
	public BData access_flags;
	public BData superclass_idx;
	public BData interfaces_off;
	public BData source_file_idx;
	public BData annotations_off;
	public BData class_data_off;
	public BData static_values_off;
	
	public DEXClassDefItem(byte[] src, int off) {
		this.offset = off;
		class_idx = new BData(off + 0, new byte[]{src[off+0],
				src[off+1],src[off+2],src[off+3]});
		access_flags = new BData(off + 4, new byte[]{src[off+4],
				src[off+5],src[off+6],src[off+7]});
		superclass_idx = new BData(off + 8, new byte[]{src[off+8],
				src[off+9],src[off+10],src[off+11]});
		interfaces_off = new BData(off + 12, new byte[]{src[off+12],
				src[off+13],src[off+14],src[off+15]});
		source_file_idx = new BData(off + 16, new byte[]{src[off+16],
				src[off+17],src[off+18],src[off+19]});
		annotations_off = new BData(off + 20, new byte[]{src[off+20],
				src[off+21],src[off+22],src[off+23]});
		class_data_off = new BData(off + 24, new byte[]{src[off+24],
				src[off+25],src[off+26],src[off+27]});
		static_values_off = new BData(off + 28, new byte[]{src[off+28],
				src[off+29],src[off+30],src[off+31]});
		this.size = class_idx.bSize + access_flags.bSize + superclass_idx.bSize +
				interfaces_off.bSize + source_file_idx.bSize + 
				annotations_off.bSize + class_data_off.bSize + 
				static_values_off.bSize;
	}
	
	@Override
	public void dump() {
		System.out.println("|------------Class Def Item:");
		System.out.println("|----------------Class Idx:\t\t" + class_idx.getInt());
		System.out.println("|----------------Access Fl:\t\t" + access_flags.getInt());
		System.out.println("|----------------Superclass Idx:\t" + superclass_idx.getInt());
		System.out.println("|----------------Interfaces off:\t" + interfaces_off.getInt());
		System.out.println("|----------------Source File Idx:\t" + source_file_idx.getInt());
		System.out.println("|----------------Annotations off:\t" + annotations_off.getInt());
		System.out.println("|----------------Class Data off:\t" + class_data_off.getInt());
		System.out.println("|----------------Static val off:\t" + static_values_off.getInt());
		System.out.println("|------------Class Def Item:");
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {class_idx, access_flags, superclass_idx,
				interfaces_off, source_file_idx, annotations_off,
				class_data_off, static_values_off};
		
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
