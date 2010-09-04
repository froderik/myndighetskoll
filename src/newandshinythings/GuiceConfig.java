package newandshinythings;

import java.util.HashMap;
import java.util.Map;

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
				
				bind(MyndighetsResource.class);
				
				serve("/*").with(GuiceContainer.class, params);
			}
		});
	}
}
