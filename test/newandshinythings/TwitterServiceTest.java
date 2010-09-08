package newandshinythings;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.junit.Before;
import org.junit.Test;

import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;


public class TwitterServiceTest {

	private TwitterService service;
	private PersistenceManager pm;
	private Twitter twitter;
	private MyndighetsRegister register;
	
	@Before
	public void construct(){
		PersistenceManagerFactory pmf = mock(PersistenceManagerFactory.class, RETURNS_MOCKS);
		twitter = mock(Twitter.class, RETURNS_MOCKS);
		register = new ScbFileParser().parse();
		
		service = new TwitterService(twitter,
				register,
				pmf);
		pm = mock(PersistenceManager.class);
		when(pmf.getPersistenceManager()).thenReturn(pm);
	}
	
	@Test
	public void testUpdate() throws Exception {
		// set up timestamp
		javax.jdo.Query q = mock(javax.jdo.Query.class);
		when(pm.newQuery(anyString())).thenReturn(q);
		ArrayList<Timestamp> stamps = new ArrayList<Timestamp>();
		Timestamp onestamp = new Timestamp(1L, 0);
		stamps.add(onestamp);
		when(q.execute(anyLong())).thenReturn(stamps);
		
		// set up twitter
		ResponseList<Status> statusar = new ResponseListMock<Status>();
		Status oneStatus = mock(Status.class,RETURNS_MOCKS);
		when(oneStatus.getCreatedAt()).thenReturn(new Date());
		when(oneStatus.getText()).thenReturn(TwitterService.OURNAME + " trafikverket : tel");
		statusar.add(oneStatus);
		when(twitter.getMentions()).thenReturn(statusar);
		
		service.update();

		verify(pm, times(1)).makePersistent(onestamp);
		verify(twitter, times(1)).updateStatus(anyString(),anyLong());

	}
}

@SuppressWarnings("serial")
class ResponseListMock<E> extends ArrayList<E> implements ResponseList<E>{

	public RateLimitStatus getFeatureSpecificRateLimitStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public RateLimitStatus getRateLimitStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	
}