package roundrobin2;

import java.util.ArrayList;
import java.util.List;

import backbone.Attribute;
import backbone.Grouping;
import backbone.StringAttribute;
import backbone.Unit;
import backbone.UnitAttribute;

public class Referee implements Unit {

	private StringAttribute _name;
	private UnitAttribute<Field> _associatedField;
	private Turn _t;
	
	public Referee(Turn t, String name){
		System.out.println(t);
		_t = t;
		_name = new StringAttribute("Name", name);
		_associatedField = new UnitAttribute<Field>("Associated Field", null, t.getFields());
	}
	@Override
	public boolean deleteFromGrouping() {
		for(Field f : _t.getFields().getMembers()){
			f.removeRef(this);
		}
		return _t.getRefs().deleteMember(this);
	}

	@Override
	public List<Attribute> getAttributes() {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(_name);
		atts.add(_associatedField);
		return atts;
	}

	@Override
	public Grouping getMemberOf() {
		return _t.getRefs();
	}

	@Override
	public String getName() {
		return this._name.value;
	}

	@Override
	public void setAttribute(Attribute attribute) {
		if(attribute.getType() == Attribute.Type.STRING)
			_name = (StringAttribute)attribute;
		else if(attribute.getType() == Attribute.Type.UNIT)
			_associatedField = (UnitAttribute)attribute;
	}

	@Override
	public void setMemberOf(Grouping<Unit> g) {
		//does nothing, as is always a member of the Referee category
	}
	@Override
	public void setName(String name) {
		this._name = new StringAttribute("Name", name);
		
	}
	public Field getField() {
		return this._associatedField.getAttribute();
	}

}
