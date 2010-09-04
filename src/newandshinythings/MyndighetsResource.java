package newandshinythings;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;


@Path("myndighet")
public class MyndighetsResource {

	private static final Logger LOG = Logger.getLogger(MyndighetsResource.class.getName());

	private final MyndighetsRegister register;
	
	private final TwitterService twitter;
	
	@Inject
	public MyndighetsResource(MyndighetsRegister register, TwitterService twitter){
		this.twitter = twitter;
		this.register = register;
		LOG.info("Create Resource");
	}
	
	@GET
	public MyndighetsRegister index(){
		return this.register;
	}

	@GET
	@Path("/twitter")
	public String updateTwitter(){
		try{
			twitter.update();
			return "WIN";
		} catch(Exception e){
			LOG.log(Level.SEVERE, "Something went wrong!", e);
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}
}
