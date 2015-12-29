
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
	public int getInt(){
		return Convertions.bytesToInt(data, 0, bSize);
	}
	public void setData(byte[] data){
		this.data = data;
		this.bSize = this.data.length;
	}
	public byte[] getData(){
		return this.data;
	}
	
}
