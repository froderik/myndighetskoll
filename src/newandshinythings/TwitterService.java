package newandshinythings;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterService {

	private static final Logger LOG = Logger.getLogger(TwitterService.class.getName());

	private final Twitter twitter;
	private final MyndighetsRegister register;

	private final String ourName = "@myndighetskoll";

	@Inject
	public TwitterService(Twitter twitter,
						  MyndighetsRegister register)
	{
		this.twitter = twitter;
		this.register = register;
	}

	public void update() throws TwitterException {

		ResponseList<Status> mentions = twitter.getMentions();

		LOG.info("Found " + mentions.size() + " mentions");
		for (Status status : mentions) {
			String text = status.getText();
			LOG.info("Mentioned: " + text);
			if(text.startsWith(ourName)){
				text = text.substring(ourName.length());
				String[] split = text.split(":");
				String searchedName = split[0].trim();
				Myndighet myndighet = register.findByName(searchedName);
				if (myndighet != null && split.length > 1) {
					String query = split[1].trim();
					parseQueryAndSendUpdate(status, myndighet, query);
				}
			}
		}
	}

	private void parseQueryAndSendUpdate(Status status, Myndighet myndighet, String query)
	throws TwitterException
	{
		StringBuilder builder = new StringBuilder("@");
		builder.append(status.getUser().getScreenName()).append(" ");
		if("epost".equalsIgnoreCase(query) || "email".equalsIgnoreCase(query)){
			LOG.info("Answering epost query for " + myndighet.getNamn());
			builder.append(myndighet.getNamn().toLowerCase()).append(" epost: ");
			builder.append(myndighet.getEpost());
		} else if("adress".equalsIgnoreCase(query) || "address".equalsIgnoreCase(query)){
			LOG.info("Answering address query for " + myndighet.getNamn());
			builder.append(myndighet.getNamn().toLowerCase()).append(" adress: ");
			builder.append(myndighet.getBesoksAdress()).append(", ");
			builder.append(myndighet.getPostAdress()).append("");
			builder.append("");
		}
		twitter.updateStatus(builder.toString(), status.getId());
	}
}
