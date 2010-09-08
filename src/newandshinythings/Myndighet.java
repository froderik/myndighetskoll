package newandshinythings;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Myndighet {
	private String orgnr;
	private String namn;
	private String epost;
	private String url;
	private String tel;
	private String fax;
	private Adress postAdress;
	private Adress besoksAdress;

	@XmlElement
	public String getOrgnr() {
		return orgnr;
	}
	public long getOrgnrAsLong() {
		String[] parts = getOrgnr().split("-");
		return Integer.parseInt(parts[0] + parts[1]);
	}
	public void setOrgnr(String orgnr) {
		this.orgnr = orgnr;
	}
	@XmlElement
	public String getNamn() {
		return namn;
	}
	public void setNamn(String namn) {
		this.namn = namn;
	}
	@XmlElement
	public String getEpost() {
		return epost;
	}
	public void setEpost(String epost) {
		this.epost = epost;
	}
	@XmlElement
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@XmlElement
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	@XmlElement
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	@XmlElement
	public Adress getPostAdress() {
		return postAdress;
	}
	public void setPostAdress(Adress postAdress) {
		this.postAdress = postAdress;
	}
	@XmlElement
	public Adress getBesoksAdress() {
		return besoksAdress;
	}
	public void setBesoksAdress(Adress besoksAdress) {
		this.besoksAdress = besoksAdress;
	}
}
