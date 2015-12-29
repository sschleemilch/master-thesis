
public abstract class Section {	
	abstract public void dump();
	abstract public byte[] getBytes();
	abstract public int getSize();
	abstract public int getOffset();
	abstract public void setOffset(int off);
}
