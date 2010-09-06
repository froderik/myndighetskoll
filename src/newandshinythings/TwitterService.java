package newandshinythings;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.google.inject.Inject;

public class TwitterService {

	private static final Logger LOG = Logger.getLogger(TwitterService.class.getName());
	private static final long ANSWER_TIMESTAMP_PK = 1L;

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
		Timestamp lastTimeSaved = getTimestamp(ANSWER_TIMESTAMP_PK);
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
			save(lastTimeSaved);
		}
	}
	
	public void retweet() throws TwitterException {
		List<Myndighet> alla = register.getMyndigheter();

		for (Myndighet myndighet : alla) {
			// TODO : put these in a task queue to make it more stable now
			// all fails if one fails
			String namn = myndighet.getNamn();
			twitter4j.Query q = new twitter4j.Query(namn);
			
			QueryResult result = twitter.search(q);
			List<Tweet> tweets = result.getTweets();
			
			Timestamp latestTimestamp = getTimestamp(myndighet.getOrgnrAsLong());
			Long newTimestamp = latestTimestamp.getMillis();

			try{
				for (Tweet tweet : tweets) {
					long tweetTimestamp = tweet.getCreatedAt().getTime();
					if(tweetTimestamp > latestTimestamp.getMillis()){
						boolean isFromUs = ourName.equalsIgnoreCase(tweet.getFromUser());
						boolean isToUs = ourName.equalsIgnoreCase(tweet.getToUser());
						if(!isFromUs && !isToUs)
						{
							// TODO : put twitter error handling centrally so all calls can benefit
							try{
								twitter.retweetStatus(tweet.getId());
							} catch(TwitterException e) {
								boolean notforbidden = e.getStatusCode() != 403;
								if(e.exceededRateLimitation() || notforbidden) throw e; // add more cases here if needed
							} finally {
								if(tweetTimestamp > newTimestamp) newTimestamp = tweetTimestamp;
							}
						}
					}
				}
			} finally {
				latestTimestamp.setMillis(newTimestamp);
				save(latestTimestamp);
			}
		}		
	}
	

	private void save(Object o){
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			pm.makePersistent(o);
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private Timestamp getTimestamp(Long primaryKey){
		PersistenceManager pm = pmf.getPersistenceManager();
		Query q = pm.newQuery("select from newandshinythings.Timestamp " +
                "where primaryKey == primaryKeyParam " +
                "parameters String primaryKeyParam ");
		try {
			List<Timestamp> tidLista = (List<Timestamp>) q.execute(primaryKey);
			if(tidLista.size() == 0) {
				return new Timestamp(primaryKey, 0);
			} else if(tidLista.size() > 1) {
				throw new IllegalStateException("There are more than one timestamp for the PK: " + primaryKey);
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
		} else if("tel".equalsIgnoreCase(query) || "tfn".equalsIgnoreCase(query)){
			LOG.info("Answering fax query for " + myndighet.getNamn());
			builder = addTel(builder, myndighet);
		} else if("orgno".equalsIgnoreCase(query) || "ssn".equalsIgnoreCase(query)){
			LOG.info("Answering ssn query for " + myndighet.getNamn());
			builder = addOrgNummer(builder, myndighet);
		}
		String message = builder.toString();
		if(message.length() > 140) message = "vårt svar blev för långt - spana in datat direkt på http://bit.ly/bngRzQ";
		twitter.updateStatus(message, status.getId());
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

	private StringBuilder addTel(StringBuilder builder, Myndighet myndighet) {
		builder.append(", Tel: ");
		builder.append(myndighet.getTel());
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
