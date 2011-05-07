package backbone;

import java.util.List;

public interface Unit {
	
	public Grouping<Unit> getMemberOf();
	
	public void setMemberOf(Grouping<Unit> g);
	
	public Unit getBlank();

	public void setAttribute(Attribute attribute);
	
	public List<Attribute> getAttributes();
	
	public String getName();
}
