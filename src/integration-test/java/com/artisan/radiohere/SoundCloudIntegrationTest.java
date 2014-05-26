package com.artisan.radiohere;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import net.avh4.test.junit.Nested;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Nested.class)
public class SoundCloudIntegrationTest {
	public class WhenTracksAreRetrieved {		
		private String tracks;

		@Before
		public void before() throws Exception {
			tracks = new SoundCloud().getTracks("Stephen Malkmus");
		}
		
		@Test
		public void shouldReturnSomething() throws Exception {
			assertThat(tracks, not(isEmptyOrNullString()));
		}
		
		@Test
		public void shouldReturnValidJSON() throws Exception {
			new JSONArray(tracks);
		}
		
		@Test
		public void shouldContainATrack() throws Exception {
			new JSONArray(tracks).getJSONObject(0).getString("title");
		}
	}
}
