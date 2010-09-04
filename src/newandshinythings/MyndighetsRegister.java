package newandshinythings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyndighetsRegister {
	private List<Myndighet> myndigheter = new ArrayList<Myndighet>();
	
	protected MyndighetsRegister(){}
	
	
	// TODO : använd lucene för att söka
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
	
	@XmlElement(name="myndighet")
	public List<Myndighet> getMyndigheter(){
		return myndigheter;
	}
}
