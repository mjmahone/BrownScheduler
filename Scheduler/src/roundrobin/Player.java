package roundrobin;

import java.util.ArrayList;
import java.util.List;

import backbone.Attribute;
import backbone.Grouping;
import backbone.StringAttribute;
import backbone.Unit;
import backbone.UnitAttribute;

public class Player implements Unit {

	private UnitAttribute<Team> _team;
	private StringAttribute _name;
	private Tournament _t;
	
	public Player(Tournament t, String name){
		_name = new StringAttribute("Name", name);
		_team = new UnitAttribute<Team>("Team", null, t.getTeams());
		_t = t;
	}
	
	public void setTeam(Team t){
		_team.att = t;
	}
	@Override
	public boolean deleteFromGrouping() {
		return _t.getPlayers().deleteMember(this);
	}

	@Override
	public List<Attribute> getAttributes() {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(_name);
		atts.add(_team);
		return atts;
	}

	@Override
	public Grouping getMemberOf() {
		return _t.getPlayers();
	}

	@Override
	public String getName() {
		return _name.toString();
	}

	/**
	 * Sets the attributes.
	 */
	@Override
	public void setAttribute(Attribute attribute) {
		if(attribute.getType() == Attribute.Type.STRING)
			_name = (StringAttribute)attribute;
		else if(attribute.getType() == Attribute.Type.UNIT){
			Team t = (Team)((UnitAttribute<Team>)attribute).att;
			t.addPlayer(this);
		}

	}

	@Override
	public void setMemberOf(Grouping<Unit> g) {
	}

	@Override
	public void setName(String name) {
		this._name = new StringAttribute("Name", name);
		
	}

}
