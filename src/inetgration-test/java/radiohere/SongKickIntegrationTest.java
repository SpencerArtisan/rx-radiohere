package radiohere;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import net.avh4.test.junit.Nested;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Nested.class)
public class SongKickIntegrationTest {
	public class WhenGigsAreRetrieved {		
		private String gigs;

		@Before
		public void before() throws Exception {
			gigs = new SongKick().getGigs(0);
		}
		
		@Test
		public void shouldReturnSomething() throws Exception {
			assertThat(gigs, not(isEmptyOrNullString()));
		}
		
		@Test
		public void shouldReturnValidJSON() throws Exception {
			new JSONObject(gigs);
		}
		
		@Test
		public void shouldContainAResultsPageNode() throws Exception {
			new JSONObject(gigs).getJSONObject("resultsPage");
		}
	}
	
	public class WhenVenueRetrieved {		
		private String venue;
		
		@Before
		public void before() throws Exception {
			venue = new SongKick().getVenue(7039);
		}
		
		@Test
		public void shouldReturnSomething() throws Exception {
			assertThat(venue, not(isEmptyOrNullString()));
		}
		
		@Test
		public void shouldReturnValidJSON() throws Exception {
			new JSONObject(venue);
		}
		
		@Test
		public void shouldContainAResultsPageNode() throws Exception {
			new JSONObject(venue).getJSONObject("resultsPage");
		}
	}
}
