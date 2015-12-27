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
	public static byte[] intToBytes(int value, int nbytes){
		byte[] res = new byte[nbytes];
		
		switch(nbytes){
		case 1:
			res[0] = (byte)value;
			break;
		case 2:
			res[0] = (byte)(value & 0xff);
			res[1] = (byte)((value >> 8) & 0xff);
			break;
		case 4:
			res[0] = (byte)(value & 0xff);
			res[1] = (byte)((value >> 8) & 0xff);
			res[2] = (byte)((value >> 16) & 0xff);
			res[3] = (byte)((value >> 24) & 0xff);
			break;
		}
		return res;
	}
}
