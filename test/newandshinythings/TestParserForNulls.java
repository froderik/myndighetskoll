package newandshinythings;

import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestParserForNulls {

	@Parameters 
	public static Collection<Object[]> methods(){
		Collection<Object[]> params = new ArrayList<Object[]>();
		
		MyndighetsRegister register = new ScbFileParser().parse();
		List<Myndighet> myndigheter = register.getMyndigheter();
		
		for (Myndighet myndighet : myndigheter) {
			params.add(new Object[]{myndighet});
		}
		
		return params;
	}
	
	private Myndighet myndighet;
	
	public TestParserForNulls(Myndighet myndighet){
		this.myndighet = myndighet;
	}
	
	@Test
	public void testReturnsNonNull() throws Exception{
		assertNotNull(myndighet.getEpost());
		assertNotNull(myndighet.getBesoksAdress());
		assertNotNull(myndighet.getBesoksAdress().getPostAdress());
		assertNotNull(myndighet.getBesoksAdress().getPostNummer());
		assertNotNull(myndighet.getBesoksAdress().getPostOrt());
		assertNotNull(myndighet.getFax());
		assertNotNull(myndighet.getNamn());
		assertNotNull(myndighet.getOrgnr());
		assertNotNull(myndighet.getPostAdress());
		assertNotNull(myndighet.getPostAdress().getPostAdress());
		assertNotNull(myndighet.getPostAdress().getPostNummer());
		assertNotNull(myndighet.getPostAdress().getPostOrt());
		assertNotNull(myndighet.getTel());
		assertNotNull(myndighet.getUrl());
	}
}
