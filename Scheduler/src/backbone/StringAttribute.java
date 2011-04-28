package backbone;

public class StringAttribute extends Attribute{
	
	public String value;

	//
	public StringAttribute(String title, String val) {
		super(title);
		this.value = val;
	}

	@Override
	public Type getType() {
		return Attribute.Type.STRING;
	}

	

}
