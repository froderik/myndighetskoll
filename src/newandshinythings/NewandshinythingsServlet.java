package newandshinythings;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

@SuppressWarnings("serial")
public class NewandshinythingsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		writer.println("Hello, world 2");

		try {
			Twitter t = new TwitterFactory().getOAuthAuthorizedInstance(
					"MnkEs2THSsf351odymOkQ",
					"TZNlpu1ElAiL9kRnv2iI55IfhqisP4n2FhxSgI5ElxY");
			Trends trends = t.getCurrentTrends();
			Trend[] trendArray = trends.getTrends();
			for (Trend trend : trendArray) {
				writer.println(trend.getName());
			}
			
			//t.updateStatus("#appengineiscool");

		} catch (TwitterException e) {
			writer.println(e.getMessage());
			e.printStackTrace(writer);
		}
	}
}
