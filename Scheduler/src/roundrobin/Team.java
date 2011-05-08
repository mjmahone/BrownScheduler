package roundrobin;
import java.util.LinkedList;

import backbone.*;


public class Team implements CompetitiveUnit {
	
	private Grouping<Unit> _category;
	
	private String _name;
	private int _wins;
	private int _losses;
	
	public Team(String name){
		this._name = name;
		_wins = 0;
		_losses = 0;
	}
	
	public void addWin(){
		_wins++;
	}
	
	public int getWins(){
		return _wins;
	}
	
	public int getLosses(){
		return _losses;
	}
	
	public void setWins(int i){
		_wins = i;
	}
	
	public String getName(){
		return this._name;
	}
	
	public LinkedList<Attribute> getAttributes(){
		
		StringAttribute name = new StringAttribute("Name", this._name);
		IntAttribute wins = new IntAttribute("Wins", _wins);
		LinkedList<Attribute> atts = new LinkedList<Attribute>();
		atts.add(name);
		atts.add(wins);
		
		return atts;
	}

	private void setStringAttribute(StringAttribute att){
		if(att.getTitle().equals("Name")){
			this._name = att.getAttribute();
		}
	}
	
	private void setIntAttribute(IntAttribute att){
		if(att.getTitle().endsWith("Wins")){
			this._wins = att.getAttribute();
		}
	}
	@Override
	public void setAttribute(Attribute attribute) {
		Attribute.Type t = attribute.getType();
		if(t == Attribute.Type.INT)
			setIntAttribute((IntAttribute)attribute);
		else if(t == Attribute.Type.STRING)
			setStringAttribute((StringAttribute)attribute);
	}
	
	public String toString(){
		String r = "Name: " + this._name;
		r += "\nWins: " + String.valueOf(this._wins);
		return r;
	}

	@Override
	public Unit getBlank() {
		return new Team("");
	}

	@Override
	public Grouping<Unit> getMemberOf() {
		return _category;
	}

	@Override
	public void setMemberOf(Grouping<Unit> g) {
		this._category = g;
		
	}


}