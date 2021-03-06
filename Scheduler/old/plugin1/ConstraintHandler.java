package plugin1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class ConstraintHandler {

	private ArrayList<Team> _teams;
	private ArrayList<Judge> _judges;
	private Tourney _t;
	
	private class TeamComparer implements Comparator<Team>{

		@Override
		public int compare(Team arg0, Team arg1) {
			return arg0.getWins() - arg1.getWins();
		}
	}
	public ConstraintHandler(Tourney t, List<Team> teams, List<Judge> judges){
		_t = t;
		this._teams = new ArrayList<Team>(teams);
		this._judges = new ArrayList<Judge>(judges);
	}
	
	private LinkedList<Team> sortTeamsByWins(){
		PriorityQueue<Team> q = new PriorityQueue<Team>(this._teams.size() + 1, new TeamComparer());
		q.addAll(this._teams);		
		LinkedList<Team> l = new LinkedList<Team>();
		while(!q.isEmpty())
			l.add(q.remove());
		return l;
	}
	
	private ArrayList<MyPairing> judgeLessPairings(){
		Random r = new Random();
		ArrayList<MyPairing> pairs = new ArrayList<MyPairing>();
		LinkedList<Team> ts = sortTeamsByWins();
		while(!ts.isEmpty()){
			Team t1 = ts.pop();
			Team t2;
			if(!ts.isEmpty()) t2 = ts.pop();
			else t2 = null;
			MyPairing toAdd = new MyPairing(_t);
			if(r.nextBoolean()){
				toAdd._gov = t1;
				toAdd._opp = t2;
			}else{
				toAdd._gov = t2;
				toAdd._opp = t1;
			}
			pairs.add(toAdd);
		}
		
		return pairs;
	}
	
	public MyRound createNewRound(){
		//shuffle the judges
		Collections.shuffle(_judges);
		//keep track of which have been used
		HashSet<Judge> usedJudges = new HashSet<Judge>();
		ArrayList<MyPairing> pairs = judgeLessPairings();
		//make all the completely valid pairings
		for(MyPairing p : pairs){
			for(Judge j : _judges){
				if(!j.hasConflict(p._gov) && !j.hasConflict(p._opp) && !usedJudges.contains(j)){
					p._judge = j;
					break;
				}
			}
		}
		//these are all the pairings that could not be created without a conflict
		MyRound r = new MyRound(_t, "a round");
		for(MyPairing p : pairs){
			if(p._judge == null && !_judges.isEmpty()){
				for(Judge j : _judges){
					if(!usedJudges.contains(j)) p._judge = j;
					break;
				}
			}
			r.addPairing(p);
		}
		return r;
	}
	//says whether there are enough judges for the round to be created
	public boolean enoughJudges(){
		return _judges.size() >= _teams.size() / 2;
	}
}
