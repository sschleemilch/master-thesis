
public class DEXAccessFlags {
	public String flag_str = "not defined";
	
	public DEXAccessFlags(int access_flags){
		switch(access_flags){
		case 0x01:
			flag_str = "public";
			break;
		case 0x02:
			flag_str = "private";
			break;
		case 0x04:
			flag_str = "protected";
			break;
		case 0x08:
			flag_str = "static";
			break;
		case 0x10:
			flag_str = "final";
			break;
		case 0x20:
			flag_str = "synchronized";
			break;
		case 0x40:
			flag_str = "volatile";
			break;
		case 0x80:
			flag_str = "transient";
			break;
		case 0x100:
			flag_str = "native";
			break;
		case 0x200:
			flag_str = "interface";
			break;
		case 0x400:
			flag_str = "abstract";
			break;
		case 0x800:
			flag_str = "strictfp";
			break;
		case 0x1000:
			flag_str = "not directly defind in source code";
			break;
		case 0x2000:
			flag_str = "annotation class";
			break;
		case 0x4000:
			flag_str = "enumeration type";
			break;
		case 0x10000:
			flag_str = "constructor method";
			break;
		case 0x20000:
			flag_str = "declared syncrhonized";
			break;
		}
	}
	
	
}
