package backbone;

public class DoubleAttribute extends Attribute {

	private Double att;
	
	public DoubleAttribute(String title){
		super(title);
		this.att = 0.0;
	}
	
	public DoubleAttribute(String title, Double att){
		super(title);
		this.att = att;
	}
	
	public Type getType() {
		return Attribute.Type.DOUBLE;
	}
	
	public double getAttribute() {
		return att;
	}
	
	public void setAttribute(double a) {
		att = a;
	}
	
	@Override
	public String toString() {
		return att.toString();
	}

}
