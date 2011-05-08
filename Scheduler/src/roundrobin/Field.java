package roundrobin;

import java.util.ArrayList;
import java.util.List;

import backbone.Attribute;
import backbone.Grouping;
import backbone.GroupingAttribute;
import backbone.StringAttribute;
import backbone.Unit;

public class Field implements Unit{

	private Grouping<Unit> _memberOf;
	private String _name;
	private Tourney _tournament;
	private RefereeGrouping _refsAssociated;
	private TeamGrouping _teamsAssociated;
	
	public Field(String name, Tourney t){
		_memberOf = t.getfields();
		_name = name;
		_refsAssociated = new RefereeGrouping("Associated Referees");
		_teamsAssociated = new TeamGrouping("Associated Teams");
		_tournament = t;
	}
	@Override
	public List<Attribute> getAttributes() {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new StringAttribute("Field Name", _name));
		atts.add(new GroupingAttribute<Team>("Associated Teams", _teamsAssociated));
		atts.add(new GroupingAttribute<Referee>("Associated Referees", _refsAssociated));
		return atts;
	}

	@Override
	public Unit getBlank() {
		return new Field("", _tournament);
	}

	@Override
	public Grouping<Unit> getMemberOf() {
		return memberOf;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(Attribute attribute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMemberOf(Grouping<Unit> g) {
		// TODO Auto-generated method stub
		
	}

}