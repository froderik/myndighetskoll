package newandshinythings;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Adress {
	private String postAdress;
	private String postNummer;
	private String postOrt;

	public Adress(){}

	public Adress(String postAdress, String postNummer, String postOrt) {
		this.postAdress = postAdress;
		this.postNummer = postNummer;
		this.postOrt = postOrt;
	}
	@XmlElement
	public String getPostAdress() {
		return postAdress;
	}
	public void setPostAdress(String postAdress) {
		this.postAdress = postAdress;
	}
	@XmlElement
	public String getPostNummer() {
		return postNummer;
	}
	public void setPostNummer(String postNummer) {
		this.postNummer = postNummer;
	}
	@XmlElement
	public String getPostOrt() {
		return postOrt;
	}
	public void setPostOrt(String postOrt) {
		this.postOrt = postOrt;
	}
}
