package newandshinythings;

import static junit.framework.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;


public class ScbFileParserTest {
	private MyndighetsRegister registret;
	private Myndighet skatteverket;

	@Before
	public void parsa() throws IOException {
		registret = new ScbFileParser().parse();
		skatteverket = registret.findByName("Skatteverket");
	}
	
	@Test 
	public void thereShallBe250Agencies(){
		assertEquals(250, registret.size() );
	}
	
	@Test
	public void skatteverketsNamn(){
		assertEquals("SKATTEVERKET", skatteverket.getNamn());
	}
	
	@Test
	public void skatteverketsOrgnr(){
		assertEquals("202100-5448", skatteverket.getOrgnr());
	}
	
	@Test
	public void skatteverketsEpost(){
		assertEquals("huvudkontoret@skatteverket.se", skatteverket.getEpost());
	}
	
	@Test
	public void skatteverketsFax(){
		assertEquals("08-280332", skatteverket.getFax());
	}
	
	@Test
	public void skatteverketsTel(){
		assertEquals("0771-778778", skatteverket.getTel());
	}
	
	@Test
	public void skatteverketsPostadress(){
		assertAdress("", "171 94", "SOLNA", skatteverket.getPostAdress());
	}
	
	@Test
	public void skatteverketsBesoksadress(){
		assertAdress("SOLNA STRANDV€G 10", "171 54", "SOLNA", skatteverket.getPostAdress());
	}

	private void assertAdress(String postadress, 
			String postnummer, 
			String postort,
			Adress actual) {
		assertEquals(postadress, actual.getPostAdress());
		assertEquals(postnummer, actual.getPostNummer());
		assertEquals(postort, actual.getPostOrt());
	}
}
