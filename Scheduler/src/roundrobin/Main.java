package roundrobin;

import java.util.*;

import backbone.Attribute;
import backbone.Category;
import backbone.Grouping;
import backbone.Pairing;
import backbone.UnitAttribute;

public class Main {
	
	
	
	public static void main(String args[]){
		Tourney t = new Tourney();
		
		Team t1 = new Team("Team 1");
		Team t2 = new Team("Team 2");
		Team t3 = new Team("Team 3");
		Team t4 = new Team("Team 4");
		Referee j1 = new Referee("Judge 1");
		Referee j2 = new Referee("Judge 2");
		
		ArrayList<Grouping> cats = t.getCategories();
		MyCategory<Team> teams = (MyCategory<Team>) cats.get(0);
		MyCategory<Referee> referees = (MyCategory<Referee>) cats.get(1);
		
		teams.addMember(t1);
		teams.addMember(t2);
		teams.addMember(t3);
		teams.addMember(t4);
		
		referees.addMember(j1);
		referees.addMember(j2);
		
		MyRound r = t.createNextRound();
		System.out.println(t.getCurrentRound().toString());
		
		MyRound r1 = t.getCurrentRound();
		ArrayList<Pairing> r1Pairs = r1.getPairings();
		Pairing p11 = r1Pairs.get(0);
		System.out.print("Is the current round finished? ");
		System.out.println(r1.isFinished());
		p11.setAttribute(new UnitAttribute<Team>("Winner", 
				(Team)((UnitAttribute<Team>)p11.getAttributes().get(0)).att));
		Pairing p12 = r1Pairs.get(1);
		p12.setAttribute(new UnitAttribute<Team>("Winner", 
				(Team)((UnitAttribute<Team>)p12.getAttributes().get(1)).att));
		
		System.out.print("Is round 1 finished? ");
		System.out.println(r.isFinished());
		System.out.print("Is the current round finished? ");
		System.out.println(r1.isFinished());
		
		System.out.println(r1);
		MyRound r2 = t.createNextRound();
		System.out.println(t.getCurrentRound().toString());
		
	}

}