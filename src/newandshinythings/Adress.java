package newandshinythings;

public class Adress {
	private String postAdress;
	private String postNummer;
	private String postOrt;
	

	public Adress(String postAdress, String postNummer, String postOrt) {
		this.postAdress = postAdress;
		this.postNummer = postNummer;
		this.postOrt = postOrt;
	}
	public String getPostAdress() {
		return postAdress;
	}
	public void setPostAdress(String postAdress) {
		this.postAdress = postAdress;
	}
	public String getPostNummer() {
		return postNummer;
	}
	public void setPostNummer(String postNummer) {
		this.postNummer = postNummer;
	}
	public String getPostOrt() {
		return postOrt;
	}
	public void setPostOrt(String postOrt) {
		this.postOrt = postOrt;
	}
}
