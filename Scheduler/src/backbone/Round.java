package backbone;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class Round implements Serializable {

	protected Collection<Pairing> pairings;
	
	public Round(int i) {
		pairings = new LinkedList<Pairing>();
	}

	public boolean isFilled() {
		return false;
	}

	public void addPairing(Pairing pairing) {
		pairings.add(pairing);
	}

}
