import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DEXStringDataItem extends Section{
	
	public int offset;
	public int size;
	
	
	public BData utf16_size_uleb;
	public BData data;
	
	
	public DEXStringDataItem(byte[]src, int off){
		this.offset = off;
		int doff = off;
		utf16_size_uleb = new BData(src, doff, "uleb128");
		doff += utf16_size_uleb.bSize;
		data = new BData(doff,
				Arrays.copyOfRange(src, doff, doff + utf16_size_uleb.getUleb128()));
		this.size = utf16_size_uleb.bSize + data.bSize;
		
	}
	public String getString(){
		return new String(data.data, StandardCharsets.UTF_8);
	}
	@Override
	public void dump() {
		System.out.println("|------------Dex String Item");
		System.out.println("|------------" + "\"" + getString() +"\"");
		System.out.println("|------------Dex String Item");
	}
	@Override
	public byte[] getBytes() {
		byte[] b = new byte[size];
		int bp = 0;
		BData[] d = {utf16_size_uleb, data};
		for(int i = 0; i < d.length; i++){
			byte[] t = d[i].data;
			for (int j = 0; j < t.length; j++){
				b[bp++] = t[j];
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
