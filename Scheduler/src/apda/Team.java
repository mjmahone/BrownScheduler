package apda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import backbone.*;


public class Team implements Unit {
	
	private String _name;
	public int wins;
	public int losses;
	public School school;
	public boolean stillInTournament;
	public int numGovs;
	public int numOpps;
	private SeedUnit _seed;
	public Debater d1;
	public Debater d2;
	private Tourney _t;
	public int byeRound = -1;
	public int pullUpRound = -1;
	public ArrayList<Team> facedBefore;
	
	public Team(Tourney t, String name){
		_name = name;
		_t = t;
	}
	
	public Team(Tourney t, String string, School s) {
		_name = string;
		_t = t;
		school = s;
	}

	public static class TeamComparator implements Serializable, Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			Team t1 = (Team)o1;
			Team t2 = (Team)o2;
			if(t1.getSpeaks() > t2.getSpeaks())return -1;
			else if(t1.getSpeaks() < t2.getSpeaks()) return 1;
			return 0;
		}
		
	}
	
	public double getSpeaks(){
		double speaks = 0;
		if(d1 != null) speaks += d1.getSpeaks();
		if(d2 != null) speaks += d2.getSpeaks();
		return speaks;
	}
	
	public double getRanks(){
		double ranks = 0;
		if(d1 != null) ranks += d1.getRanks();
		if(d2 != null) ranks += d2.getRanks();
		return ranks;
	}
	public boolean isFullSeed(){
		return _seed.getName().equals("Full");
	}
	public boolean isHalfSeed(){
		return _seed.getName().equals("Half");
	}
	public boolean isFreeSeed(){
		return _seed.getName().equals("Full");
	}
	
	public void setSeed(String seedName){
		if(seedName.equals("Full"))
			_seed = new SeedUnit("Full");
		else if(seedName.equals("Half"))
			_seed = new SeedUnit("Half");
		if(seedName.equals("Free"))
			_seed = new SeedUnit("Free");
	}
	@Override
	public boolean deleteFromGrouping() {
		if(d1 != null) this.d1.deleteFromGrouping();
		if(d2 != null) this.d2.deleteFromGrouping();
		_t._teams._members.remove(this);
		return school.removeTeam(this);
	}

	@Override
	public List<Attribute> getAttributes() {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new UnitAttribute<School>("School", school, _t._schools));
		atts.add(new StringAttribute("Name", _name));
		
		DebaterGrouping deb1 = new DebaterGrouping(_t, "deb1 grouping");
		if(d1 != null) deb1.addMember(d1);
		DebaterGrouping deb2 = new DebaterGrouping(_t, "deb2 grouping");
		if(d2 != null) deb2.addMember(d2);
		atts.add(new UnitAttribute<Debater>("Debater 1", d1, deb1));
		atts.add(new UnitAttribute<Debater>("Debater 2", d2, deb2));
		return atts;
	}

	@Override
	public Grouping getMemberOf() {
		return _t._teams;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setAttribute(Attribute att) {
		if(att.getTitle().equals("Name")){
			_name = ((StringAttribute)att).value;
		}
		else if(att.getTitle().equals("Debater 1")){
			UnitAttribute<Debater> debAtt = (UnitAttribute<Debater>)att;
			d1 = debAtt.att;
		}
		else if(att.getTitle().equals("Debater 2")){
			UnitAttribute<Debater> debAtt = (UnitAttribute<Debater>)att;
			d2 = debAtt.att;
		}
		else if(att.getTitle().equals("School")){
			UnitAttribute<School> schoolAtt = (UnitAttribute<School>)att;
			if(school != null) school.removeTeam(this);
			school = schoolAtt.att;
			if(school != null) school.addTeam(this);
		}
		else if(att.getTitle().equals("In Tournament")){
			stillInTournament = ((BooleanAttribute)att).getAttribute();
		}
		else if(att.getTitle().equals("Seed")){
			SeedUnit newSeed = ((UnitAttribute<SeedUnit>)att).att;
			if(newSeed == null) _seed = null;
			else if(newSeed.getName().equals("")) _seed = null;
			else if(newSeed.getName().equals("Full") ||
					newSeed.getName().equals("Half") ||
					newSeed.getName().equals("Free")){
				_seed = newSeed;
				if(newSeed.getName().equals("Free") && school != null)
					school._freeSeed = this;
			}
		}
	}

	@Override
	public void setMemberOf(Grouping<Unit> g) {		
	}

	@Override
	public void setName(String name) {
		_name = name;
	}


}
