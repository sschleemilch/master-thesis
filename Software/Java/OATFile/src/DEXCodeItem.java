
public class DEXCodeItem extends Section{
	public int size;
	public int offset;
	
	public BData registers_size;
	public BData ins_size;
	public BData outs_size;
	public BData tries_size;
	public BData debug_info_off;
	public BData insns_size;
	public BData[] insns;
	public BData padding;
	
	public DEXTryItem[] tries;
	public DEXEncodedCatchHandlerList handlers;
	
	
	public DEXCodeItem(byte[]src, int off) {
		this.offset = off;
		int doff = off;
		registers_size = new BData(doff, new byte[]{src[doff+0],src[doff+1]});
		doff += registers_size.bSize;
		ins_size = new BData(doff, new byte[]{src[doff+0],src[doff+1]});
		doff += ins_size.bSize;
		outs_size = new BData(doff, new byte[]{src[doff+0],src[doff+1]});
		doff += outs_size.bSize;
		tries_size = new BData(doff, new byte[]{src[doff+0],src[doff+1]});
		doff += tries_size.bSize;
		debug_info_off = new BData(doff, new byte[]{src[doff+0],src[doff+1],
				src[doff+2],src[doff+3]});
		doff += debug_info_off.bSize;
		insns_size = new BData(doff, new byte[]{src[doff+0],src[doff+1],
				src[doff+2],src[doff+3]});
		doff += insns_size.bSize;
		insns = new BData[insns_size.getInt()];
		for (int i = 0; i < insns.length; i++){
			insns[i] = new BData(doff, new byte[]{src[doff+0],src[doff+1]});
			doff += insns[i].bSize;
		}
		
		if (tries_size.getInt()!= 0 && insns_size.getInt()%2 != 0){
			padding = new BData(doff, new byte[]{src[doff + 0], src[doff + 1]});
			doff += padding.bSize;
		}
		
		if (tries_size.getInt() != 0){
			tries = new DEXTryItem[tries_size.getInt()];
			for ( int i = 0; i < tries.length; i++){
				tries[i] = new DEXTryItem(src, doff);
				doff += tries[i].getSize();
			}
			handlers = new DEXEncodedCatchHandlerList(src, doff);
		}
		this.size = doff - off;
	}
	
	
	@Override
	public void dump() {
		System.out.print("|--------------------Offset:\t\t");
		System.out.printf("0x%08X\n", this.offset);
		System.out.println("|--------------------Registers used:\t" + registers_size.getInt());
		System.out.println("|--------------------n-Arguments In:\t" + ins_size.getInt());
		System.out.println("|--------------------n-Arguments Out:\t" + outs_size.getInt());
		System.out.println("|--------------------nTry-Items:\t" + tries_size.getInt());
		System.out.print("|----------------------Debug Info Off:\t");
		System.out.printf("0x%08X\n", debug_info_off.getInt());
		System.out.println("|--------------------Instr Block Size:\t" + insns_size.getInt());
		System.out.println("|--------------------Instructions:");
		int c = 0;
		for (int i = 0; i < insns.length; i++){
			if (i == 0){
				System.out.print("\t\t\t");
			}
			System.out.printf("0x%04X\t",insns[i].getInt());
			c++;
			if (c==4){
				c=0;
				System.out.println();
				System.out.print("\t\t\t");
			}
		}
		System.out.println();
		if(padding != null){
			System.out.println("|--------------------Padding(2 bytes):\tTrue");
		}else{
			System.out.println("|--------------------Padding(2 bytes):\tFalse");
		}
		if (tries_size.getInt() != 0){
			System.out.println("|--------------------Try Items there:\tTrue");
			for (int i = 0; i < tries.length; i++){
				tries[i].dump();
			}
			System.out.println("|--------------------Handlers there:\tTrue");
			handlers.dump();
		}else{
			System.out.println("|--------------------Try Items there:\tFalse");
			System.out.println("|--------------------Handlers there:\tFalse");
		}
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[size];
		BData[] bd = {registers_size, ins_size, outs_size, tries_size,
				debug_info_off, insns_size};
		int bp = 0;
		for (int i = 0; i < bd.length; i++){
			for(int j = 0; j < bd[i].bSize; j++){
				b[bp++] = bd[i].data[j];
			}
		}
		for (int i = 0; i < insns.length; i++){
			byte[] tmp = insns[i].data;
			for(int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
			}
		}
		byte [] tmp;
		if (padding != null){
			tmp = padding.data;
			for (int i = 0; i < tmp.length; i++){
				b[bp++] = tmp[i];
			}
		}
		if (tries != null){
			for (int i = 0; i < tries.length; i++){
				tmp = tries[i].getBytes();
				for (int j = 0; j < tmp.length; j++){
					b[bp++] = tmp[j];
				}
			}
		}
		if (handlers != null){
			tmp = handlers.getBytes();
			for (int i = 0; i < tries.length; i++){
				tmp = tries[i].getBytes();
				for (int j = 0; j < tmp.length; j++){
					b[bp++] = tmp[j];
				}
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
