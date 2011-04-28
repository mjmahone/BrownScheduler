package backbone;

import java.util.List;

public abstract class Unit extends Grunit{
	
	protected String _name;
	
	public Unit(String name){
		_name = name;
	}
	
	public abstract List<Attribute> getAttributes();

}