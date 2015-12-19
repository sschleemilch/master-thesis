import java.util.Arrays;

public class Convertions {
	
	//Extracting an int as LSB at offset
	public static int bytesToInt(byte[] src, int offset, int nBytes){
		byte[] tmp = Arrays.copyOfRange(src, offset, offset+nBytes);
		if (nBytes == 2){
			return (tmp[0]&0xFF) | ((tmp[1]&0xFF) << 8);
		}
		if (nBytes == 4){
			return java.nio.ByteBuffer.wrap(tmp).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
		}
		return -1;
	}
}
