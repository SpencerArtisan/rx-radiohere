package com.artisan.radiohere;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Nested.class)
public class JsonGigsTest {
	private List<Gig> gigs;
	
	public class WithNoPerformances {
		private static final String gigsJson = 
				"{                                                        " +
				"  'resultsPage': {                                       " +
				"    'results': { 'event': [                              " +
				"      {                                                  " +
				"        'start':{'date':'2012-04-18'},                   " +
				"        'performance':[],     							 " +
				"        'venue':{'displayName':'The Fillmore', 'lat': 51, 'lng': 1}   " +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
	                                                                                                                                  
		@Before
		public void before() throws Exception {
			gigs = new JsonGigs().extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs.size(), is(1));
		}

		@Test
		public void shouldProvideANullArtist() throws Exception {
			assertThat(gigs.get(0).getArtist(), nullValue());
		}
	}
	
	public class WithOneGig {
		private static final String gigsJson = 
				"{                                                        " +
				"  'resultsPage': {                                       " +
				"    'results': { 'event': [                              " +
				"      {                                                  " +
				"        'start':{'date':'2012-04-18'},                   " +
				"        'performance':[     							 " +
				"           {											 " +
				"			  'displayName': 'Wild Flag',     			 " +
				"             'artist': {'id': 1},     					 " +
				"           }     										 " +
				"        ],     											 " +
				"        'venue':{'displayName':'The Fillmore', 'lat': 52, 'lng': 1}  " +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
	                                                                                                                                  
		@Before
		public void before() throws Exception {
			gigs = new JsonGigs().extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs.size(), is(1));
		}
		
		@Test
		public void shouldProvideTheArtist() throws Exception {
			assertThat(gigs.get(0).getArtist(), equalTo("Wild Flag"));
		}
		
		@Test
		public void shouldProvideTheArtistId() throws Exception {
			assertThat(gigs.get(0).getSongkickArtistId(), equalTo(1));
		}

		@Test
		public void shouldProvideTheDate() throws Exception {
			assertThat(gigs.get(0).getDate(), equalTo("2012-04-18"));
		}
		
		@Test
		public void shouldProvideTheVenueName() throws Exception {
			assertThat(gigs.get(0).getVenueName(), equalTo("The Fillmore"));
		}

		@Test
		public void shouldProvideALocation() throws Exception {
			assertThat(gigs.get(0).getVenue(), is(new Coordinate(52, 1)));
		}
	}
	
	public class WithNullLatLong {
		private static final String gigsJson = 
				"{                                                        " +
				"  'resultsPage': {                                       " +
				"    'results': { 'event': [                              " +
				"      {                                                  " +
				"        'start':{'date':'2012-04-18'},                   " +
				"        'performance':[     							 " +
				"           {											 " +
				"			  'displayName': 'Wild Flag',     			 " +
				"             'artist': {'id': 1},     					 " +
				"           }     										 " +
				"        ],     											 " +
				"        'venue':{'displayName':'The Fillmore', 'lat':null, 'lng':null}" +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
		
		@Before
		public void before() throws Exception {
			gigs = new JsonGigs().extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs.size(), is(1));
		}

		@Test
		public void shouldProvideANullLocation() throws Exception {
			assertThat(gigs.get(0).getVenue(), nullValue());
		}
	}
	
	public class WithoutLatLong {
		private static final String gigsJson = 
				"{                                                        " +
						"  'resultsPage': {                                       " +
						"    'results': { 'event': [                              " +
						"      {                                                  " +
						"        'start':{'date':'2012-04-18'},                   " +
						"        'performance':[     							 " +
						"           {											 " +
						"			  'displayName': 'Wild Flag',     			 " +
						"             'artist': {'id': 1},     					 " +
						"           }     										 " +
						"        ],     											 " +
						"        'venue':{'displayName':'The Fillmore'}" +
						"      }                                                  " +
						"    ]}                                                   " +
						"  }                                                      " +
						"}  														 ";
		
		@Before
		public void before() throws Exception {
			gigs = new JsonGigs().extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs.size(), is(1));
		}
		
		@Test
		public void shouldProvideANullLocation() throws Exception {
			assertThat(gigs.get(0).getVenue(), nullValue());
		}
	}
}
