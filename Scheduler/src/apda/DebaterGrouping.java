package apda;

import java.util.List;

import backbone.Grouping;

public class DebaterGrouping implements Grouping<Debater> {

	private String _name;
	private Tourney _t;
	
	public DebaterGrouping(Tourney tourney, String name) {
		_t = tourney;
		_name = name;
	}

	@Override
	public void addMember(Debater member) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteMember(Debater member) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Debater getBlank() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Debater> getMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
