package roundrobin;

import plugin1.MyCategory;

public class TeamGrouping extends MyCategory<Team> {

	public TeamGrouping(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Team getBlank() {
		// TODO Auto-generated method stub
		return null;
	}

}
