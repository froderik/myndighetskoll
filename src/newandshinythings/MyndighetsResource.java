package newandshinythings;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("myndighet")
public class MyndighetsResource {

	@GET
	public String index(){
		return "Index page reached";
	}
}
