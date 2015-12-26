
public abstract class ELFSection {	
	abstract public void dump();
	abstract public byte[] getBytes();
	abstract public int getSize();
	abstract public int getOffset();
}
