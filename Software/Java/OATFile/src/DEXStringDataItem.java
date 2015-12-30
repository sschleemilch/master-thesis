import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DEXStringDataItem {
	
	public int offset;
	public int size;
	
	BData utf16_size;
	BData data;
	
	public DEXStringDataItem(byte[]src, int off){
		utf16_size = new BData(src, off, "uleb128");
		data = new BData(off + utf16_size.bSize,
				Arrays.copyOfRange(src, off + utf16_size.bSize,
						off + utf16_size.bSize + utf16_size.getUleb128()));
		
	}
	public String getString(){
		return new String(data.data, StandardCharsets.UTF_8);
	}
}
