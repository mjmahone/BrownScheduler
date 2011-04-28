package plugin1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import backbone.Category;


public class Tourney extends backbone.Tournament{
	
	private ArrayList<plugin1.MyRound> rounds;
	private LinkedList<Team> competitors;
	private LinkedList<Judge> judges;
	private int totalRounds;

	public Collection<backbone.CompetitiveUnit> getCompetitors() {
		LinkedList<backbone.CompetitiveUnit> t = new LinkedList<backbone.CompetitiveUnit>();
		t.addAll(competitors);
		return t;
	}
	
	@Override
	public MyRound getCurrentRound() {
		if(rounds == null){
			rounds = new ArrayList<MyRound>();
			rounds.add(new MyRound(1));
		}
		int r = rounds.size() - 1;
		plugin1.MyRound currRound = rounds.get(r);
		if(currRound != null){
			if(!currRound.isFinished() || currRound.getRoundNum() >= totalRounds){
				return currRound;
			}
			else{
				MyRound nextRound = new MyRound(r + 1);
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
		teams.addAll(this.competitors);
		Collections.shuffle(teams);
		LinkedList<Judge> judges = new LinkedList<Judge>();
		judges.addAll(this.judges);
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
		return round;
	}

	
	public Collection<backbone.Category> getCategories() {
		LinkedList<Category> cats = new LinkedList<Category>();
		Category<Team> compets = new Category<Team>("Teams");
		Category<Judge> judgs = new Category<Judge>("Judges");
		cats.add(compets);
		cats.add(judgs);
		return cats;
	}
	
	public Collection<backbone.Round> getRounds() {
		LinkedList<backbone.Round> rs = new LinkedList<backbone.Round>();
		rs.addAll(rounds);
		return rs;
	}
}
