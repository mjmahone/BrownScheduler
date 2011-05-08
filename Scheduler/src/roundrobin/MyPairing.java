package roundrobin;

import java.util.LinkedList;
import java.util.List;

import backbone.*;

/**
 * Class that holds each pairing,
 * which is where the actual individual competitions take place.
 * @author matt
 *
 */
public class MyPairing implements backbone.Pairing{

	Team _gov;
	Team _opp;
	Referee _judge;
	Team _winner;
	
	public MyPairing(){
		_gov = null;
		_opp = null;
		_judge = null;
		_winner = null;
	}
	
	public MyPairing(Team gov, Team opp, Referee referee) {
		_gov = gov;
		_opp = opp;
		_judge = referee;
	}
	
	public void setWinner(Team t){
		_winner = t;
		_winner.setWins(_winner.getWins() + 1);
	}
	
	public boolean isFinished(){
		return _winner != null;
	}
	
	public String toString(){
		String r = "";
		r += "Gov: ";
		if(_gov != null){
			r += _gov.getName();
		}
		r += "\nOpp: ";
		if(_opp != null){
			r += _opp.getName();
		}
		r+= "\nJudge: ";
		if(_judge != null){
			r += _judge.getName();
		}
		r += "\nWinner: ";
		if(_winner != null){
			r += _winner.getName();
		}
		return r +"\n";
	}

	@Override
	public List<Attribute> getAttributes() {
		LinkedList<Attribute> atts = new LinkedList<Attribute>();
		atts.add(new UnitAttribute<Team>("Gov", _gov));
		atts.add(new UnitAttribute<Team>("Opp", _opp));
		RefereeGrouping judgeGroup = new RefereeGrouping("Judges");
		atts.add(new UnitAttribute<Referee>("Judge", _judge));
		TeamGrouping teamGroup = new TeamGrouping("Teams");
		if(_gov != null) teamGroup.addMember(_gov);
		if(_opp != null) teamGroup.addMember(_opp);
		atts.add(new UnitAttribute<Team>("Winner", _winner, teamGroup));
		return atts;
	}

	private void setUnitAttribute(UnitAttribute<Unit> att){
		String title = att.getTitle();
		if(title.equals("Gov")) _gov = (Team)att.att;
		else if(title.equals("Opp")) _opp = (Team)att.att;
		else if(title.equals("Judge")) _judge = (Referee)att.att;
		else if(title.equals("Winner")){
			Team newWinner = (Team)att.att;
			if(_winner == null){
				_winner = newWinner;
				_winner.setWins(_winner.getWins() + 1);
				_judge._conflictedTeams.add(_gov);
				_judge._conflictedTeams.add(_opp);
			}else if(_winner == newWinner){}
			else{
				_winner.setWins(_winner.getWins() - 1);
				_winner = newWinner;
				_winner.setWins(_winner.getWins() + 1);
				_judge._conflictedTeams.add(_gov);
				_judge._conflictedTeams.add(_opp);
			}
			
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setAttribute(Attribute attribute) {
		if(attribute.getType() == Attribute.Type.UNIT) 
			setUnitAttribute((UnitAttribute<Unit>)attribute);
		
	}

	@Override
	public String getName() {
		return "Pairing";
	}

	@Override
	public Unit getBlank() {
		// TODO Auto-generated method stub
		return new MyPairing();
	}

	@Override
	public Grouping getMemberOf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMemberOf(Grouping g) {
		// TODO Auto-generated method stub
		
	}

}