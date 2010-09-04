package newandshinythings;

public class Myndighet {
	private String orgnr;
	private String namn;
	private String epost;
	private String url;
	private String tel;
	private String fax;
	private Adress postAdress;
	private Adress besoksAdress;

	public String getOrgnr() {
		return orgnr;
	}
	public void setOrgnr(String orgnr) {
		this.orgnr = orgnr;
	}
	public String getNamn() {
		return namn;
	}
	public void setNamn(String namn) {
		this.namn = namn;
	}
	public String getEpost() {
		return epost;
	}
	public void setEpost(String epost) {
		this.epost = epost;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public Adress getPostAdress() {
		return postAdress;
	}
	public void setPostAdress(Adress postAdress) {
		this.postAdress = postAdress;
	}
	public Adress getBesoksAdress() {
		return besoksAdress;
	}
	public void setBesoksAdress(Adress besoksAdress) {
		this.besoksAdress = besoksAdress;
	}
}
