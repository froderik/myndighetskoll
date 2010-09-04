package newandshinythings;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("myndighet")
public class MyndighetsResource {

	private static final Logger LOG = Logger.getLogger(MyndighetsResource.class.getName());
	
	public MyndighetsResource(){
		LOG.info("Create Resource");
	}
	
	@GET
	public String index(){
		return "Index page reached";
	}
}
