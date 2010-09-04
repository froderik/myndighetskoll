package newandshinythings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class ScbFileParser {
	public MyndighetsRegister parse() throws IOException {
		// TODO : lŠs frŒn livekŠllan
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream("myndighetsregistret.txt");
			InputStreamReader isr = new InputStreamReader(is);
			LineNumberReader lnr = new LineNumberReader(isr);

			String currentLine = lnr.readLine(); // skip first line
			MyndighetsRegister register = new MyndighetsRegister();
			while (true) {
				currentLine = lnr.readLine();
				if(currentLine == null) break;
				Myndighet enMyndighet = readOneLine(currentLine);
				register.add(enMyndighet);
			}
			return register;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public Myndighet readOneLine(String oneLine){
		String[] parts = oneLine.split("\t");
		Myndighet myndighet = new Myndighet();
		myndighet.setOrgnr(get(0,parts));
		myndighet.setNamn(get(1,parts));
		myndighet.setEpost(get(10,parts));
		myndighet.setTel(get(8,parts));
		myndighet.setFax(get(9,parts));
		myndighet.setPostAdress(get(2,3,4,parts));
		myndighet.setBesoksAdress(get(5,6,7,parts));
		return myndighet;
	}
	
	private Adress get(int adressIndex, int postnrIndex, int postortIndex, String[] parts){
		return new Adress(get(adressIndex,parts), get(postnrIndex,parts), get(postortIndex,parts));
	}
	
	private String get(int index, String[] parts){
		return index >= parts.length ? "" : parts[index];
	}
}
