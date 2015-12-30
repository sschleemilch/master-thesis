
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

	public static int uLeb128Size(int value) {
        int remaining = value >> 7;
        int count = 0;

        while (remaining != 0) {
            remaining >>= 7;
            count++;
        }

        return count + 1;
    }
	
	public static int sLeb128Size(int value) {
        int remaining = value >> 7;
        int count = 0;
        boolean hasMore = true;
        int end = ((value & Integer.MIN_VALUE) == 0) ? 0 : -1;

        while (hasMore) {
            hasMore = (remaining != end)
                || ((remaining & 1) != ((value >> 6) & 1));

            value = remaining;
            remaining >>= 7;
            count++;
        }

        return count;
    }


	public void setUleb128(int value){
		int remaining = value >>> 7;
		byte[]b = new byte[uLeb128Size(value)];
		int bp=0;
		
        while (remaining != 0) {
            b[bp++] = ((byte) ((value & 0x7f) | 0x80));
            value = remaining;
            remaining >>>= 7;
        }

        b[bp++] = ((byte) (value & 0x7f));
        data = b;
	}
	public void setSleb128(int value){
		byte[] b = new byte[sLeb128Size(value)];
		int bp = 0;
		
		int remaining = value >> 7;
        boolean hasMore = true;
        int end = ((value & Integer.MIN_VALUE) == 0) ? 0 : -1;

        while (hasMore) {
            hasMore = (remaining != end)
                    || ((remaining & 1) != ((value >> 6) & 1));

            b[bp++] = ((byte) ((value & 0x7f) | (hasMore ? 0x80 : 0)));
            value = remaining;
            remaining >>= 7;
        }
        data = b;
	}
	
	public int getUleb128(){
		int result = 0;
        int cur;
        int count = 0;
        int dp = 0;
        do {
            cur = data[dp++] & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);

        return result;
	}
	public int getSleb128(){
		int result = 0;
        int cur;
        int count = 0;
        int signBits = -1;
        int dp = 0;
        
        do {
            cur = data[dp++] & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            signBits <<= 7;
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);

        // Sign extend if appropriate
        if (((signBits >> 1) & result) != 0 ) {
            result |= signBits;
        }

        return result;
	}
	
}
