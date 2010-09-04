package newandshinythings;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;

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
				//TODO Replace with ProvidersO
				TwitterFactory twitterFactory = new TwitterFactory();
			
				String consumerKey = "GVjNjNVxiWWIRMsj6NH5A";
				String consumerSecret = "fRRsZL42qAxnKmrv2PkzQXaT9r0KLTJbjIwKXxh07IU";
				String token = "186786197-59kXltM4xdLoUbhBg8WbzwG0cdl8wlZugNLeJ9gN";
				String tokenSecret = "IpBejWRMFJq1B0N9tV89tBpwN3Ho1kU8krIaJNmZ2UU";
				AccessToken accessToken = new AccessToken(token, tokenSecret);
				Twitter twitter = twitterFactory.getOAuthAuthorizedInstance(consumerKey, consumerSecret, accessToken);
				bind(Twitter.class).toInstance(twitter);
				bind(TwitterService.class);
				
				ScbFileParser p = new ScbFileParser();
				bind(MyndighetsRegister.class).toInstance(p.parse());
				bind(MyndighetsResource.class);
				
				PersistenceManager pm = 
					JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
				bind(PersistenceManager.class).toInstance(pm);

				
				serve("/*").with(GuiceContainer.class, params);
			}
		});
	}
}
