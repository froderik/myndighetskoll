package newandshinythings;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.google.inject.Inject;

public class TwitterService {

	private static final Logger LOG = Logger.getLogger(TwitterService.class.getName());

	private final Twitter twitter;
	private final MyndighetsRegister register;
	private final PersistenceManagerFactory pmf;

	private final String ourName = "@myndighetskoll";

	@Inject
	public TwitterService(Twitter twitter,
						  MyndighetsRegister register,
						  PersistenceManagerFactory pmf)
	{
		this.twitter = twitter;
		this.register = register;
		this.pmf = pmf;
	}

	public void update() throws TwitterException {

		ResponseList<Status> mentions = twitter.getMentions();
		Timestamp lastTimeSaved = getTimestamp();
		long newest = lastTimeSaved.getMillis();

		LOG.info("Found " + mentions.size() + " mentions");
		try{
			for (Status status : mentions) {
				long mentionTime = status.getCreatedAt().getTime();
				if(mentionTime > lastTimeSaved.getMillis())
				{
					if(mentionTime > newest){
						newest = mentionTime;
					}
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
		} finally {
			lastTimeSaved.setMillis(newest);
			saveTimestamp(lastTimeSaved);
		}
	}

	private void saveTimestamp(Timestamp lastTimeSaved) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			pm.makePersistent(lastTimeSaved);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private Timestamp getTimestamp(){
		PersistenceManager pm = pmf.getPersistenceManager();
		Query q = pm.newQuery(Timestamp.class);
		try{
			List<Timestamp> tidLista = (List<Timestamp>) q.execute();
			if(tidLista.size() == 0)
			{
				return new Timestamp(0);
			}
			return tidLista.get(0);
		} finally {
			q.closeAll();
			pm.close();
		}
	}

	private void parseQueryAndSendUpdate(Status status, Myndighet myndighet, String query)
	throws TwitterException
	{
		StringBuilder builder = new StringBuilder("@");
		builder.append(status.getUser().getScreenName()).append(" ");
		builder.append(myndighet.getNamn().toLowerCase());
		if("epost".equalsIgnoreCase(query) || "email".equalsIgnoreCase(query)){
			LOG.info("Answering epost query for " + myndighet.getNamn());
			builder = addEmail(builder, myndighet);
		} else if("adress".equalsIgnoreCase(query) || "address".equalsIgnoreCase(query)){
			LOG.info("Answering address query for " + myndighet.getNamn());
			builder = addPostAddress(builder, myndighet);
		} else if("besoksaddress".equalsIgnoreCase(query)){
			LOG.info("Answering address query for " + myndighet.getNamn());
			builder = addBesoksAddress(builder, myndighet);
		} else if("hemsida".equalsIgnoreCase(query) || "site".equalsIgnoreCase(query)){
			LOG.info("Answering site query for " + myndighet.getNamn());
			builder = addSite(builder, myndighet);
		} else if("fax".equalsIgnoreCase(query)){
			LOG.info("Answering fax query for " + myndighet.getNamn());
			builder = addFax(builder, myndighet);
		} else if("orgno".equalsIgnoreCase(query) || "ssn".equalsIgnoreCase(query)){
			LOG.info("Answering ssn query for " + myndighet.getNamn());
			builder = addOrgNummer(builder, myndighet);
		}else if("all".equalsIgnoreCase(query)){
			LOG.info("Answering all query for " + myndighet.getNamn());
			builder = addEmail(builder, myndighet);
			builder = addPostAddress(builder, myndighet);
			builder = addBesoksAddress(builder, myndighet);
			builder = addSite(builder, myndighet);
			builder = addFax(builder, myndighet);
			builder = addOrgNummer(builder, myndighet);
		}
		twitter.updateStatus(builder.toString(), status.getId());
	}

	private StringBuilder addOrgNummer(StringBuilder builder, Myndighet myndighet) {
		builder.append(", orgno: ");
		builder.append(myndighet.getOrgnr());
		return builder;
	}

	private StringBuilder addFax(StringBuilder builder, Myndighet myndighet) {
		builder.append(", fax: ");
		builder.append(myndighet.getFax());
		return builder;
	}

	private StringBuilder addSite(StringBuilder builder, Myndighet myndighet) {
		builder.append(", site: ");
		builder.append(myndighet.getUrl());
		return builder;
	}

	private StringBuilder addPostAddress(StringBuilder builder, Myndighet myndighet) {
		builder.append(", adress: ");
		builder.append(myndighet.getPostAdress());
		return builder;
	}

	private StringBuilder addBesoksAddress(StringBuilder builder, Myndighet myndighet) {
		builder.append(", adress: ");
		builder.append(myndighet.getBesoksAdress()).append(", ");
		return builder;
	}

	private StringBuilder addEmail(StringBuilder builder, Myndighet myndighet) {
		builder.append(", epost: ");
		builder.append(myndighet.getEpost());
		return builder;
	}
}
