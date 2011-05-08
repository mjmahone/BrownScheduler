package roundrobin;

import backbone.Grouping;
import backbone.Unit;

public class FieldGrouping extends MyCategory<Field> implements Grouping<Field> {

	private Tourney t; 
	public FieldGrouping(String name, Tourney t) {
		super(name);
		this.t = t;
	}

	@Override
	public Field getBlank() {
		// TODO Auto-generated method stub
		return new Field("", t);
	}

}
