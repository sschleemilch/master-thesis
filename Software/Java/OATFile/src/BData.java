
public class BData {
	public int off;
	public int bSize;
	public byte[] data;
	
	public BData(int offset, byte[] data){
		this.off = offset;
		this.data = data;
		this.bSize = data.length;
	}
	public void setInt(int value){
		byte[] bytes = Convertions.intToBytes(value, bSize);
		for (int i = 0; i < bytes.length; i++){
			data[i] = bytes[i];
		}
	}
	
}
