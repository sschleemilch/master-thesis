
public class DEXMapItem extends ELFSection{
	public static final int TYPE_HEADER_ITEM = 0;
	public static final int TYPE_STRING_ID_ITEM = 1;
	public static final int TYPE_TYPE_ID_ITEM = 2;
	public static final int TYPE_PROTO_ID_ITEM = 3;
	public static final int TYPE_FIELD_ID_ITEM = 4;
	public static final int TYPE_METHOD_ID_ITEM = 5;
	public static final int TYPE_CLASS_DEF_ITEM = 6;
	public static final int TYPE_MAP_LIST = 0x1000;
	public static final int TYPE_TYPE_LIST = 0x1001;
	public static final int TYPE_ANNOTATION_SET_REF_LIST = 0x1002;
	public static final int TYPE_ANNOTATION_SET_ITEM = 0x1003;
	public static final int TYPE_CLASS_DATA_ITEM = 0x2000;
	public static final int TYPE_CODE_ITEM = 0x2001;
	public static final int TYPE_STRING_DATA_ITEM = 0x2002;
	public static final int TYPE_DEBUG_INFO_ITEM = 0x2003;
	public static final int TYPE_ANNOTATION_ITEM = 0x2004;
	public static final int TYPE_ENCODED_ARRAY_ITEM = 0x2005;
	public static final int TYPE_ANNOTATIONS_DIRECTORY_ITEM = 0x2006;
	
	
	public BData type;
	public BData unused;
	public BData bsize;
	public BData boffset;
	
	private int size;
	private int offset;
	
	public DEXMapItem(byte[] src, int off){
		type = new BData(off + 0 , new byte[]{src[off+0],src[off+1]});
		unused = new BData(off + 2 , new byte[]{src[off+2],src[off+3]});
		bsize = new BData(off + 4, new byte[]{src[off+4], src[off+5],
				src[off+6], src[off+7]});
		boffset = new BData(off + 8, new byte[]{src[off+8], src[off+9],
				src[off+10], src[off+11]});
		
		size = type.bSize + unused.bSize + bsize.bSize + boffset.bSize;
		offset = off;
		
	}
	
	public void dump(){
		System.out.println("|------------Dex Map Item");
		System.out.print("|----------------Type:\t\t");
		switch(Convertions.bytesToInt(type.data, 0, type.bSize)){
		case TYPE_HEADER_ITEM:
			System.out.println("Header Item");
			break;
		case TYPE_STRING_ID_ITEM:
			System.out.println("String ID");
			break;
		case TYPE_TYPE_ID_ITEM:
			System.out.println("Type ID");
			break;
		case TYPE_PROTO_ID_ITEM:
			System.out.println("Proto ID");
			break;
		case TYPE_FIELD_ID_ITEM:
			System.out.println("Field ID");
			break;
		case TYPE_METHOD_ID_ITEM:
			System.out.println("Method ID");
			break;
		case TYPE_CLASS_DEF_ITEM:
			System.out.println("Class Def");
			break;
		case TYPE_MAP_LIST:
			System.out.println("Map List");
			break;
		case TYPE_TYPE_LIST:
			System.out.println("Type List");
			break;
		case TYPE_ANNOTATION_SET_REF_LIST:
			System.out.println("Annot. Set Ref List");
			break;
		case TYPE_ANNOTATION_SET_ITEM:
			System.out.println("Annot. Set Item");
			break;
		case TYPE_CLASS_DATA_ITEM:
			System.out.println("Class Data Item");
			break;
		case TYPE_CODE_ITEM:
			System.out.println("Code Item");
			break;
		case TYPE_STRING_DATA_ITEM:
			System.out.println("String Data Item");
			break;
		case TYPE_DEBUG_INFO_ITEM:
			System.out.println("Debug Info Item");
			break;
		case TYPE_ANNOTATION_ITEM:
			System.out.println("Annot. Item");
			break;
		case TYPE_ENCODED_ARRAY_ITEM:
			System.out.println("Encoded Array Item");
			break;
		case TYPE_ANNOTATIONS_DIRECTORY_ITEM:
			System.out.println("Annot. Dir Item");
			break;
		default:
			System.out.println("Unknown");
		}
		System.out.println("|----------------N-Items:\t" + Convertions.bytesToInt(bsize.data, 0, bsize.bSize));
		System.out.print("|----------------Offset(Dex):\t");
		System.out.printf("0x%08X\n",Convertions.bytesToInt(boffset.data, 0, boffset.bSize));
		System.out.println("|------------Dex Map Item");
	}

	@Override
	public byte[] getBytes() {
		BData[] bd = {type, unused, bsize, boffset};
		
		byte[]bytes = new byte[size];
		
		int bp = 0;
		for (int i = 0; i < bd.length; i++){
			for (int j = 0; j < bd[i].bSize; j++){
				bytes[bp++] = bd[i].data[j];
			}
		}
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
