package plugin1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

<<<<<<< HEAD
import old_backbone.Grouping;
import old_backbone.Pairing;

=======
import backbone.Grouping;
import backbone.Pairing;
>>>>>>> a000f12bcde518994143eb0d8852f1066cd087a2

/**
 * This class is a Grouping of pairings.
 * 
 * @author matt
 *
 */
<<<<<<< HEAD
public class MyRound extends old_backbone.Round{
=======
public class MyRound extends backbone.Round{
>>>>>>> a000f12bcde518994143eb0d8852f1066cd087a2
	
	private int _roundNum;
	
	public MyRound(int i) {
		super(i);
		_roundNum = i;
	}
	
	public boolean isFinished(){
<<<<<<< HEAD
		for(old_backbone.Pairing p : this.pairings){
=======
		for(backbone.Pairing p : this.pairings){
>>>>>>> a000f12bcde518994143eb0d8852f1066cd087a2
			if(((MyPairing) p).isFinished()){
				return true;
			}
		}
		return false;
	}
	
	public int getRoundNum(){
		return _roundNum;
	}
	
	public void addPairing(MyPairing p){
		this.pairings.add(p);
	}
	
	public String toString(){
		
		String r = "";
		for(Pairing p : this.pairings){
			r = r + ((MyPairing) p).toString();
			r = r + "\n";
		}
		return r;
	}

}
