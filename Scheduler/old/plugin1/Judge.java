package plugin1;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import backbone.Attribute;
import backbone.Grouping;
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
	
	
	HashSet<Team> _conflictedTeams;
	private Grouping<Unit> _category;
	private String _name;
	private Tourney _t;
	
	public Judge(Tourney t, String name) {
		this._name = name;
		_conflictedTeams = new HashSet<Team>();
		_t = t;
		_category = _t.getCategories().get(1);
	}

	@Override
	public List<Attribute> getAttributes() {
	
		LinkedList<Attribute> atts = new LinkedList<Attribute>();
		StringAttribute name = new StringAttribute("Name", this._name);
		GroupingAttribute<Team> conflicts = new GroupingAttribute<Team>("Conflicted Teams", 
				new TeamGrouping(_t, "Conflicted Teams", new LinkedList<Team>(_conflictedTeams)));
		atts.add(name);
		atts.add(conflicts);
		return atts;
	}
	
	@Override
	public String getName(){
		return this._name;
	}
	
	public boolean hasConflict(Team t){
		return _conflictedTeams.contains(t);
	}

	@Override
	public void setAttribute(Attribute attribute) {
		if(attribute.getType() == Attribute.Type.STRING){
			StringAttribute att = (StringAttribute)attribute;
			this._name = att.getAttribute();
		}
		else if(attribute.getType() == Attribute.Type.GROUPING){
			this._conflictedTeams = new HashSet<Team>(((GroupingAttribute<Team>)attribute).getMembers());
			for(Team t : _conflictedTeams) {
				Team t2 = null;
				for(Object o : _t.getCategories().get(0).getMembers()) {
					Team team = (Team) o;
					if(team.getName().equals(t.getName())) {
						t2 = team;
						break;
					}
				}
				if(t2 == null) {
					_t.getCategories().get(0).addMember(t);
				} else {
					t2.setAttribute(t.getAttributes().get(1));
				}
			}
		}
		
	}
	
	public void addConflictedTeam(Team t){
		this._conflictedTeams.add(t);
	}

	@Override
	public String toString(){
		String r = "Name: " + this._name;
		r += " Conflicts: ";
		for(Team t : this._conflictedTeams){
			r += t.getName() + "; ";
		}
		
		return r;
	}

	@Override
	public Grouping<Unit> getMemberOf() {
		return this._category;
	}

	@Override
	public void setMemberOf(Grouping<Unit> g) {
		this._category = g;
		
	}

	@Override
	public boolean deleteFromGrouping() {
		return _category.deleteMember(this);
		
	}

	@Override
	public void setName(String name) {
		this._name = name;
		
	}
	
}
