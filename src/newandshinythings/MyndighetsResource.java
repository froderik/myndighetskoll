package newandshinythings;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.inject.Inject;


@Path("myndighet")
public class MyndighetsResource {

	private static final Logger LOG = Logger.getLogger(MyndighetsResource.class.getName());
	private final MyndighetsRegister register;
	
	@Inject
	public MyndighetsResource(MyndighetsRegister register){
		LOG.info("Create Resource");
		this.register = register;
	}
	
	@GET
	public MyndighetsRegister index(){
		return this.register;
	}
	
	
}
