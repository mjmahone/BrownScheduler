package plugin1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import backbone.Grouping;

/**
 * This class is a Grouping of pairings.
 * 
 * @author matt
 *
 */
public class Round implements Grouping{
	
	private ArrayList<Team> _teams;
	private ArrayList<Judge> _judges;
	private ArrayList<Pairing> _pairings;
	
	public Round(List<Team> teams, List<Judge> judges){
		_teams = new ArrayList<Team>(teams);
		_judges = new ArrayList<Judge>(judges);
		
		Collections.shuffle(_teams);
		Collections.shuffle(_judges);
		for(int i = 0; i < _teams.size(); i += 2){
			Team o = null;
			if(i + 1 < _teams.size()){
				o = _teams.get(i + 1);
			}
			Judge j = null;
			if(i / 2 < _judges.size()){
				j = _judges.get(i / 2);
			}
			Pairing pair = new Pairing(_teams.get(i), o, j);
			_pairings.add(pair);
		}
		
	}

}
