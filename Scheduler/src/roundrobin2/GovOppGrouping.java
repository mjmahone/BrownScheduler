package roundrobin2;

public class GovOppGrouping extends MyGrouping<GovOppUnit>{

	public GovOppGrouping(String name) {
		super(name);
	}

	@Override
	public GovOppUnit getBlank() {
		// TODO Auto-generated method stub
		return new GovOppUnit();
	}

	@Override
	public void clear() {
		this._members.clear();
	}

}
