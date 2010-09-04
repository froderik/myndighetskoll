package newandshinythings;

import java.util.ArrayList;
import java.util.List;

public class MyndighetsRegister {
	private List<Myndighet> myndigheter = new ArrayList<Myndighet>();
	
	protected MyndighetsRegister(){}
	
	
	// TODO : anv�nd lucene f�r att s�ka
	public Myndighet findByName(String namn){
		for (Myndighet enMyndighet : myndigheter) {
			if(enMyndighet.getNamn().equalsIgnoreCase(namn)){
				return enMyndighet;
			}
		}
		return null;
	}

	public int size() {
		return myndigheter.size();
	}
	
	protected void add(Myndighet myndighet){
		myndigheter.add(myndighet);
	}
}
