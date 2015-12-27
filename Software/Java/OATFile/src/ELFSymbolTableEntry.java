
public class ELFSymbolTableEntry extends ELFSection{
	public BData name;
	public BData value;
	public BData bsize;
	public BData info;
	public BData other;
	public BData shndx;
	public String sName;
	
	private int size;
	private int offset;
	
	public ELFSymbolTableEntry(byte[] src, int off){
		name = new BData(off+0, new byte[]{src[off+0],src[off+1],src[off+2],src[off+3]});
		value = new BData(off+4, new byte[]{src[off+4],src[off+5],src[off+6],src[off+7]});
		bsize = new BData(off+8, new byte[]{src[off+8],src[off+9],src[off+10],src[off+11]});
		info = new BData(off+12, new byte[]{src[off+12]});
		other = new BData(off+13, new byte[]{src[off+13]});
		shndx = new BData(off+14, new byte[]{src[off+14],src[off+15]});
		
		size = name.bSize + value.bSize + bsize.bSize + info.bSize + other.bSize + shndx.bSize;
		offset = off;
	}
	
	public void dump(){
		System.out.println("|----Symbol Table Entry");
		System.out.println("|--------Name:\t\t\t"+sName);
		System.out.println("|--------Value:\t\t\t" + Convertions.bytesToInt(value.data, 0, value.bSize));
		System.out.print("|--------Size:\t\t\t");
		System.out.printf("0x%08X\n", Convertions.bytesToInt(bsize.data, 0, bsize.bSize));
		System.out.println("|--------Section Table Index:\t" + 
				Convertions.bytesToInt(shndx.data, 0, shndx.bSize));
		
		int inf = Convertions.bytesToInt(info.data, 0, info.bSize);	
		System.out.print("|--------Symbol Binding:\t");
		switch(st_bind(inf)){
		case 0:
			System.out.println("LOCAL");
			break;
		case 1:
			System.out.println("GLOBAL");
			break;
		case 2:
			System.out.println("WEAK");
			break;
		case 13:
			System.out.println("CPU Semantics");
			break;
		case 15:
			System.out.println("CPU Semantics");
			break;
		default:
			System.out.println("Unknown");
		}
		System.out.print("|--------Symbol Types:\t\t");
		switch(st_type(inf)){
		case 0:
			System.out.println("Not Specified");
			break;
		case 1:
			System.out.println("Data Object (Variable, Array, ...)");
			break;
		case 2:
			System.out.print("Function or executable Code");
			break;
		case 3:
			System.out.println("Section");
			break;
		case 4:
			System.out.println("Filename");
			break;
		case 13:
			System.out.println("CPU Semantics");
			break;
		case 15:
			System.out.println("CPU Semantics");
			break;
		}
		System.out.println("|----Symbol Table Entry");
	}
	
	private int st_bind(int i){
		return (i>>4);
	}
	private int st_type(int i){
		return (i&0xf);
	}
	private int st_info(int b, int t){
		return ((b<<4) + (t&0xf));
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {name, value, bsize, info, other, shndx};
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
		offset = off;
	}
}
