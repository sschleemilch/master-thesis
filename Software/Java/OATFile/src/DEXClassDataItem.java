
public class DEXClassDataItem extends Section{

	public int offset;
	public int size;
	
	public BData static_fields_size_uleb;
	public BData instance_fields_size_uleb;
	public BData direct_method_size_uleb;
	public BData virtual_method_size_uleb;
	
	public DEXEncodedField[] static_fields;
	public DEXEncodedField[] instance_fields;
	public DEXEncodedMethod[] direct_methods;
	public DEXEncodedMethod[] virtual_methods;
	
	
	public DEXClassDataItem(byte[]src, int off) {
		int doff = off;

		static_fields_size_uleb = new BData(src, doff, "uleb128");
		doff += static_fields_size_uleb.bSize;
		instance_fields_size_uleb = new BData(src, doff, "uleb128");
		doff += instance_fields_size_uleb.bSize;
		direct_method_size_uleb = new BData(src, doff, "uleb128");
		doff += direct_method_size_uleb.bSize;
		virtual_method_size_uleb = new BData(src, doff, "uleb128");
		doff += virtual_method_size_uleb.bSize;
		
		static_fields = new DEXEncodedField[static_fields_size_uleb.getUleb128()];
		instance_fields = new DEXEncodedField[instance_fields_size_uleb.getUleb128()];
		direct_methods = new DEXEncodedMethod[direct_method_size_uleb.getUleb128()];
		virtual_methods = new DEXEncodedMethod[virtual_method_size_uleb.getUleb128()];
		
		for(int i = 0; i < static_fields.length; i++){
			static_fields[i] = new DEXEncodedField(src, doff);
			doff+=static_fields[i].getSize();
		}
		for(int i = 0; i < instance_fields.length; i++){
			instance_fields[i] = new DEXEncodedField(src, doff);
			doff+=instance_fields[i].getSize();
		}
		for(int i = 0; i < direct_methods.length; i++){
			direct_methods[i] = new DEXEncodedMethod(src, doff);
			doff+=direct_methods[i].getSize();
		}
		for(int i = 0; i < virtual_methods.length; i++){
			virtual_methods[i] = new DEXEncodedMethod(src, doff);
			doff+=virtual_methods[i].getSize();
		}
		this.offset = off;
		this.size = doff - off;
	}
	
	@Override
	public void dump() {
		System.out.println("|------------Class Data Item");
		
		System.out.println("|----------------Static Field Size:\t" + static_fields_size_uleb.getUleb128());
		System.out.println("|----------------Instance Field Size:\t" + instance_fields_size_uleb.getUleb128());
		System.out.println("|----------------Direct Methods Size:\t" + direct_method_size_uleb.getUleb128());
		System.out.println("|----------------Virtual Methods Size:\t" + virtual_method_size_uleb.getUleb128());
		
		System.out.println("|----------------Static Fields");
		for(int i = 0; i < static_fields.length; i++){
			static_fields[i].dump();
		}
		System.out.println("|----------------Static Fields");
		System.out.println("|----------------Instance Fields");
		for(int i = 0; i < instance_fields.length; i++){
			instance_fields[i].dump();
		}
		System.out.println("|----------------Instance Fields");
		System.out.println("|----------------Direct Methods");
		for(int i = 0; i < direct_methods.length; i++){
			direct_methods[i].dump();
		}
		System.out.println("|----------------Direct Methods");
		System.out.println("|----------------Virtual Methods");
		for(int i = 0; i < virtual_methods.length; i++){
			virtual_methods[i].dump();
		}
		System.out.println("|----------------Virtual Methods");
		
		System.out.println("|------------Class Data Item");
	}

	@Override
	public byte[] getBytes() {
		byte[] b = new byte[size];
		int bp = 0;
		BData[] d = {static_fields_size_uleb, instance_fields_size_uleb,
				direct_method_size_uleb, virtual_method_size_uleb};
		for(int i = 0; i < d.length; i++){
			byte[] tmp = d[i].data;
			for (int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
			}
		}
		for(int i = 0; i < static_fields.length; i++){
			byte[]tmp = static_fields[i].getBytes();
			for(int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
			}
		}
		for(int i = 0; i < instance_fields.length; i++){
			byte[]tmp = instance_fields[i].getBytes();
			for(int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
			}
		}
		for(int i = 0; i < direct_methods.length; i++){
			byte[]tmp = direct_methods[i].getBytes();
			for(int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
			}
		}
		for(int i = 0; i < virtual_methods.length; i++){
			byte[]tmp = virtual_methods[i].getBytes();
			for(int j = 0; j < tmp.length; j++){
				b[bp++] = tmp[j];
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
