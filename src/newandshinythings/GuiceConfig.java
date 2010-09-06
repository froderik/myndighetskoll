package newandshinythings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "newandshinythings");

		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				
				PersistenceManagerFactory pmf = 
					JDOHelper.getPersistenceManagerFactory("transactions-optional");
				bind(PersistenceManagerFactory.class).toInstance(pmf);

				//TODO Replace with ProvidersO
				TwitterFactory twitterFactory = new TwitterFactory();

				TwitterAccountConfig accountConfig = loadAccountConfig(pmf);
				bind(TwitterAccountConfig.class).toInstance(accountConfig);
				AccessToken accessToken = new AccessToken(accountConfig.getToken(),
														  accountConfig.getTokenSecret());
				Twitter twitter = twitterFactory.getOAuthAuthorizedInstance(accountConfig.getConsumerKey(),
																			accountConfig.getConsumerSecret(),
																			accessToken);
				bind(Twitter.class).toInstance(twitter);
				bind(TwitterService.class);
				
				ScbFileParser p = new ScbFileParser();
				bind(MyndighetsRegister.class).toInstance(p.parse());
				bind(MyndighetsResource.class);
			
				serve("/*").with(GuiceContainer.class, params);
			}

			@SuppressWarnings("unchecked")
			private TwitterAccountConfig loadAccountConfig(
					PersistenceManagerFactory pmf) {
				PersistenceManager pm = pmf.getPersistenceManager();
				Query q = pm.newQuery(TwitterAccountConfig.class);
				try{
					List<TwitterAccountConfig> accs = (List<TwitterAccountConfig>) q.execute();
					if(!accs.isEmpty())
					{
						return accs.get(0);
					}
				} finally {
					q.closeAll();
					pm.close();
				}
				throw new IllegalStateException("Can't run without twitter account information!");
			}

			private void persistCredentials(PersistenceManagerFactory pmf,
					String consumerKey, String consumerSecret, String token,
					String tokenSecret) {
				PersistenceManager pm = pmf.getPersistenceManager();
				try {
					pm.makePersistent(new TwitterAccountConfig(consumerKey,consumerSecret,token,tokenSecret));
				} finally {
					pm.close();
				}
			}
		});
	}
}
