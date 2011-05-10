package roundrobin;

import plugin1.MyCategory;

public class PlayerGrouping extends MyCategory<Player> {

	private Tournament _t;
	private Team _team;
	public PlayerGrouping(Tournament t, Team team, String name) {
		super(name);
		_t = t;
		_team = team;
	}

	@Override
	public Player getBlank() {
		Player p = new Player(_t, "");
		p.setTeam(_team);
		return p;
	}

}
