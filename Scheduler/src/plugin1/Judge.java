package plugin1;

import java.util.LinkedList;
import java.util.List;

import backbone.Attribute;
import backbone.GroupingAttribute;
import backbone.StringAttribute;
import backbone.Unit;


/**
 * Just a judge. 
 * One is necessary for a proper pairing to occur
 * 
 * @author matt
 *
 */
public class Judge implements Unit{
	
	
	LinkedList<Team> conflictedTeams;
	private String _name;
	
	public Judge(String name) {
		this._name = name;
		conflictedTeams = new LinkedList<Team>();
	}

	@Override
	public List<Attribute> getAttributes() {
	
		LinkedList<Attribute> atts = new LinkedList<Attribute>();
		StringAttribute name = new StringAttribute("Name", this._name);
		GroupingAttribute conflicts = new GroupingAttribute("Conflicted Teams");
		atts.add(name);
		atts.add(conflicts);
		return atts;
	}
	
	public String getName(){
		return this._name;
	}

	@Override
	public void setAttribute(Attribute attribute) {
		// TODO Auto-generated method stub
		
	}
	
}
