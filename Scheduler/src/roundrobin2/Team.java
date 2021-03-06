package roundrobin2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import backbone.Attribute;
import backbone.Grouping;
import backbone.GroupingAttribute;
import backbone.IntAttribute;
import backbone.StringAttribute;
import backbone.Unit;

public class Team implements Unit {

	private Turn _t;
	private StringAttribute _name;
	private IntAttribute _gamesWon;
	private IntAttribute _gamesLost;
	private GroupingAttribute<Player> _players;
	private HashMap<Team, Integer> _teamsFaced;

	public Team(Turn t, String name){
		_t = t;
		_name = new StringAttribute("Name", name);
		_gamesWon = new IntAttribute("Wins", 0);
		_gamesLost = new IntAttribute("Losses", 0);
		_players = new GroupingAttribute<Player>("Players", new PlayerGrouping(_t, "Players"));
		_teamsFaced = new HashMap<Team, Integer>();
	}

	public void addFacedTeam(Team t, int i){
		_teamsFaced.put(t, i);
	}

	public void removeFacedTeam(Team t){
		_teamsFaced.remove(t);
	}

	public boolean hasfacedBefore(Team t, int i){
		Integer whenFaced = Integer.MAX_VALUE;
		if(_teamsFaced.containsKey(t)){
			whenFaced = _teamsFaced.get(t);
		}
		return i > whenFaced;
	}

	@Override
	public boolean deleteFromGrouping() {
		return _t.getTeams().deleteMember(this);
	}

	@Override
	public List<Attribute> getAttributes() {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(_name);
		atts.add(_gamesWon);
		atts.add(_gamesLost);
		atts.add(_players);
		return atts;
	}

	@Override
	public Grouping getMemberOf() {
		return _t.getTeams();
	}

	@Override
	public String getName() {
		return _name.value;
	}

	@Override
	public void setAttribute(Attribute attribute) {
		if(attribute.getType() == Attribute.Type.STRING)
			_name = (StringAttribute)attribute;
		else if(attribute.getType() == Attribute.Type.GROUPING){
			_players = (GroupingAttribute<Player>)attribute;
		}
		else if(attribute.getType() == Attribute.Type.INT){
			IntAttribute att = (IntAttribute)attribute;
			if(att.getTitle().equals("Wins")){
				_gamesWon = att;
			}else if(att.getTitle().equals("Losses")) _gamesLost = att;
		}

	}

	public void setGamesWon(int i){
		this._gamesWon = new IntAttribute("Wins", i);
	}

	public int getGamesWon(){
		return this._gamesWon.getAttribute();
	}

	public void setGamesLost(int i){
		this._gamesLost = new IntAttribute("Losses", i);
	}

	public int getGamesLost(){
		return this._gamesLost.getAttribute();
	}

	@Override
	public void setMemberOf(Grouping<Unit> g) {
		// DO NOTHING

	}

	public void addPlayer(Player player) {
		if(!hasPlayer(player)){
			this._players.getMembers().add(player);
		}
	}

	public void removePlayer(Player player){
		if(hasPlayer(player)) this._players.getMembers().remove(player);
	}

	public boolean hasPlayer(Player player) {
		System.out.println("hi");
		for(Team t : _t.getTeams().getMembers()) {
			System.out.println(t==this);
			if(t != this)
				for(Player p : (((GroupingAttribute<Player>) t.getAttributes().get(3)).getMembers()))
					if(p.getName().equals(player.getName()))
						return true;
		}
		return false;
	}

	@Override
	public void setName(String name) {
		this._name = new StringAttribute("Name", name);

	}

}
