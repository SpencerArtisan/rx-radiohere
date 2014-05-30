package com.artisan.radiohere;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import net.avh4.test.junit.Nested;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Nested.class)
public class EchoNestIntegrationTest {
	public class WhenArtistIsRetrieved {		
		private String artist;

		@Before
		public void before() throws Exception {
			artist = new EchoNest().getArtist(569763);
		}
		
		@Test
		public void shouldReturnSomething() throws Exception {
			assertThat(artist, not(isEmptyOrNullString()));
		}
		
		@Test
		public void shouldReturnValidJSON() throws Exception {
			new JSONObject(artist);
		}
		
		@Test
		public void shouldContainAResponseNode() throws Exception {
			new JSONObject(artist).getJSONObject("response");
		}
	}
}
