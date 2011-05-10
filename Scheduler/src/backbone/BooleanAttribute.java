package backbone;

/**
 * This is a wrapper class for a boolean
 * It holds a boolean and a title
 * (so that the GUI/io can display it properly)
 * 
 * @author pclay
 *
 */
public class BooleanAttribute extends Attribute {

	//Just the boolean that is wrapped
	private boolean att;
	
	/**
	 * @deprecated
	 * 
	 * @param title sets the title label
	 */
	@Deprecated
	public BooleanAttribute(String title) {
		super(title);
		this.att = false;
	}
	
	/**
	 * Sets the title and attribute
	 * 
	 * @param title title of the Attribute
	 * @param att true of false
	 */
	public BooleanAttribute(String title, boolean att) {
		super(title);
		this.att = att;
	}
	
	/**
	 * Returns that ti's a Type.BOOLEAN
	 * 
	 * Used primarily for checking and ensuring valid casts
	 */
	@Override
	public Type getType() {
		return Attribute.Type.BOOLEAN;
	}

	/**
	 * 
	 * @return the boolean this Attribute holds
	 */
	public boolean getAttribute() {
		return att;
	}
	
	/**
	 * 
	 * @param a the boolean value to set
	 */
	public void setAttribute(boolean a) {
		att = a;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(att);
	}
}
