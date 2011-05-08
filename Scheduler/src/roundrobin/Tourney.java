package roundrobin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import middleend.MiddleEnd;
import backbone.Grouping;


@SuppressWarnings("serial")
public class Tourney implements backbone.Tournament{
	
	private ArrayList<MyRound> rounds;
	private TeamGrouping teams;
	private RefereeGrouping refs;
	private FieldGrouping fields;
	private int totalRounds;
	
	public Tourney(){
		rounds = new ArrayList<MyRound>();
		teams = new TeamGrouping("Competitors");
		refs = new RefereeGrouping("Judges");
		totalRounds = 2;
	}

	
	@Override
	public MyRound getCurrentRound() {
		if(rounds == null){
			rounds = new ArrayList<MyRound>();
			rounds.add(new MyRound("first round"));
		}
		int r = rounds.size() - 1;
		MyRound currRound = rounds.get(r);
		if(currRound != null){
			if(!currRound.isFinished() || currRound.getRoundNum() >= totalRounds){
				return currRound;
			}
			else{
				MyRound nextRound = new MyRound("another round");
				rounds.add(nextRound);
				return nextRound;
			}
		}
		return null;
	}
	
	@Override
	public MyRound createNextRound(){
		ConstraintHandler handle = new ConstraintHandler(this.teams.getMembers(), 
				this.refs.getMembers());
		MyRound r = handle.createNewRound();
		rounds.add(r);
		return r;
	}

	
	@SuppressWarnings("unchecked")
	public ArrayList<Grouping> getCategories() {
		ArrayList<Grouping> cats = new ArrayList<Grouping>();
		cats.add(this.teams);
		cats.add(this.refs);
		cats.add(this.fields);
		return cats;
	}
	
	public FieldGrouping getfields(){
		return this.fields;
	}
	
	public TeamGrouping getTeams(){
		return this.teams;
	}
	
	public RefereeGrouping getRefs(){
		return this.refs;
	}
	
	public List<backbone.Round> getRounds() {
		LinkedList<backbone.Round> rs = new LinkedList<backbone.Round>();
		rs.addAll(rounds);
		return rs;
	}
	
	public static void main(String[] args) {
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (UnsupportedLookAndFeelException e) {
		    // handle exception
		} catch (ClassNotFoundException e) {
		    // handle exception
		} catch (InstantiationException e) {
		    // handle exception
		} catch (IllegalAccessException e) {
		    // handle exception
		}
		Tourney t = new Tourney();
		new MiddleEnd(t);
	}
}