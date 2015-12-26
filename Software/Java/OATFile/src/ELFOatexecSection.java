
public class ELFOatexecSection extends ELFSection{
	public byte[] bytes;
	
	private int size;
	private int offset;
	
	public ELFOatexecSection(byte[] src, int off, int size){
		bytes = new byte[size];
		this.size = size;
		offset = off;
		for (int i = 0; i < bytes.length; i++){
			bytes[i] = src[off + i];
		}
		
	}

	public void setNewContent(byte[] nbytes){
		bytes = nbytes;
		size = bytes.length;
	}
	
	@Override
	public void dump() {
		System.out.println("|");
		System.out.println("|--Oatexe Section");
		System.out.print("|----Offset:\t");
		System.out.printf("0x%08X\n", offset);
		System.out.print("|----Size:\t");
		System.out.printf("0x%08X\n", size);
		System.out.println("|--Oatexe Section");
	}

	@Override
	public byte[] getBytes() {
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
