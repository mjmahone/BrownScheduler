package plugin1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import middleend.MiddleEnd;
import backbone.Grouping;


@SuppressWarnings("serial")
public class Tourney implements backbone.Tournament{
	
	private ArrayList<plugin1.MyRound> rounds;
	private TeamGrouping competitors;
	private JudgeGrouping judges;
	private int totalRounds;
	
	public Tourney(){
		rounds = new ArrayList<MyRound>();
		competitors = new TeamGrouping("Competitors");
		judges = new JudgeGrouping("Judges");
		totalRounds = 2;
	}

	
	@Override
	public MyRound getCurrentRound() {
		if(rounds == null){
			rounds = new ArrayList<MyRound>();
			rounds.add(new MyRound(0));
		}
		int r = rounds.size() - 1;
		plugin1.MyRound currRound = rounds.get(r);
		if(currRound != null){
			if(!currRound.isFinished() || currRound.getRoundNum() >= totalRounds){
				return currRound;
			}
			else{
				MyRound nextRound = new MyRound(0);
				rounds.add(nextRound);
				return nextRound;
			}
		}
		return null;
	}
	
	@Override
	public MyRound createNextRound(){
		int num = rounds.size();
		LinkedList<Team> teams = new LinkedList<Team>();
		teams.addAll(competitors.getMembers());
		Collections.shuffle(teams);
		LinkedList<Judge> judges = new LinkedList<Judge>();
		judges.addAll(this.judges.getMembers());
		Collections.shuffle(judges);
		MyRound round = new MyRound(num);
		
		while(!teams.isEmpty()){
			Team gov = teams.remove();
			Team opp = null;
			if(!teams.isEmpty()) opp = teams.remove();
			Judge j = null;
			if(!judges.isEmpty()) j = judges.remove();
			MyPairing pair = new MyPairing(gov, opp, j);
			round.addPairing(pair);
		}
		this.rounds.add(round);
		return round;
	}

	
	@SuppressWarnings("unchecked")
	public ArrayList<Grouping> getCategories() {
		ArrayList<Grouping> cats = new ArrayList<Grouping>();
		cats.add(this.competitors);
		cats.add(this.judges);
		return cats;
	}
	
	public Collection<backbone.Round> getRounds() {
		LinkedList<backbone.Round> rs = new LinkedList<backbone.Round>();
		rs.addAll(rounds);
		return rs;
	}
	
	public static void main(String[] args) {
		Tourney t = new Tourney();
		new MiddleEnd(t);
	}
}
