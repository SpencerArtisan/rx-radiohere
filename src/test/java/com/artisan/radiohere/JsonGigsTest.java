package com.artisan.radiohere;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;

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
				"        'venue':{'displayName':'The Fillmore','id':42}   " +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
	                                                                                                                                  
		@Before
		public void before() throws Exception {
			gigs = new JsonGigs(gigsJson).extract().toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
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
				"        'venue':{'displayName':'The Fillmore', 'id':42}  " +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
	                                                                                                                                  
		@Before
		public void before() throws Exception {
			gigs = new JsonGigs(gigsJson).extract().toList().toBlockingObservable().single();
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
		public void shouldProvideTheVenueId() throws Exception {
			assertThat(gigs.get(0).getVenueId(), equalTo(42));
		}
	}
	
	public class WithUnknownVenue {
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
				"        'venue':{'displayName':'The Fillmore', 'id':null}" +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
		
		@Before
		public void before() throws Exception {
			gigs = new JsonGigs(gigsJson).extract().toList().toBlockingObservable().single();
		}
		
//		@Test
//		public void shouldObserveNoGigs() throws Exception {
//			assertThat(gigs, empty());
//		}
	}
}
