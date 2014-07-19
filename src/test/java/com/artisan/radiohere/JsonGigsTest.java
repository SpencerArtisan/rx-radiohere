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
	private SongKick songKick;
	private GigObservableFactory gigObservableFactory;
	private VenueObservableFactory venueObservableFactory;

	@Before
	public void before() {
		songKick = mock(SongKick.class);
		venueObservableFactory = mock(VenueObservableFactory.class);
	}

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
	                                                                                                                                  
		private List<Gig> gigs;
	        
		@Before
		public void before() throws Exception {
			gigObservableFactory = spy(new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0));
			when(songKick.getGigs(0)).thenReturn(gigsJson);
			when(gigObservableFactory.songKickToGigs(gigsJson)).thenReturn(Observable.empty());
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithApiException {
		private Observable<Gig> observable;
		
		@Before
		public void before() throws Exception {
			gigObservableFactory = new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0);
			when(songKick.getGigs(0)).thenThrow(new ApiException("", new Throwable()));
			observable = gigObservableFactory.create();
		}
		
		@Test
		public void shouldAbortTheObservable() throws Exception {
			observable.subscribe((gig) -> fail("Should not call onNext"),
								 (ex) ->	 {},
								 () -> fail("Should not complete without error"));
					
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
	                                                                                                                                  
		private List<Gig> gigs;
		private Venue venue;
	        
		@Before
		public void before() throws Exception {
			gigObservableFactory = new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0);
			venue = new Venue("name", "postcode", new Coordinate(51.5, -0.1));
			when(venueObservableFactory.create(42)).thenReturn(Observable.just(venue));
			when(songKick.getGigs(0)).thenReturn(gigsJson);
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
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
		
		@Test
		public void shouldProvideTheVenue() throws Exception {
			assertThat(gigs.get(0).getVenue(), equalTo(venue));
		}

		@Test
		public void shouldProvideTheVenueDistance() throws Exception {
			assertThat(gigs.get(0).getVenueDistance(Coordinate.OLD_STREET), closeTo(3.185, 0.006));
		}

		@Test
		public void shouldProvideTheDistanceFromOldStreet() throws Exception {
			assertThat(gigs.get(0).getDistance(), closeTo(3.185, 0.006));
		}
	}

	public class WithOneGigALongWayAway {
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
	                                                                                                                                  
		private List<Gig> gigs;
		private Venue venue;
	        
		@Before
		public void before() throws Exception {
			gigObservableFactory = new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0);
			venue = new Venue("name", "postcode", new Coordinate(12, 30));
			when(venueObservableFactory.create(42)).thenReturn(Observable.just(venue));
			when(songKick.getGigs(0)).thenReturn(gigsJson);
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
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
		
		private List<Gig> gigs;
		
		@Before
		public void before() throws Exception {
			when(venueObservableFactory.create(null)).thenReturn(Observable.empty());
			gigObservableFactory = new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0);
			when(songKick.getGigs(0)).thenReturn(gigsJson);
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithManyGigsSameArtistDifferentDate {
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
				"        'venue':{'displayName':'The Fillmore','id':42}   " +
				"      },                                                 " +
				"      {                                                  " +
				"        'start':{'date':'2012-04-19'},                   " +
				"        'performance':[     							 " +
				"           {											 " +
				"			  'displayName': 'Wild Flag',     		     " +
				"             'artist': {'id': 1},     					 " +
				"           }     										 " +
				"        ],     											 " +
				"        'venue':{'displayName':'The Fillmore','id':42}   " +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
		
		private List<Gig> gigs;
		private Venue venue1;
		
		@Before
		public void before() throws Exception {
			gigObservableFactory = new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0);
			venue1 = new Venue("name1", "postcode1", new Coordinate(51, 1));
			when(venueObservableFactory.create(42)).thenReturn(Observable.just(venue1));
			when(songKick.getGigs(0)).thenReturn(gigsJson);
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs.size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItem(new Gig("Wild Flag", 1, "2012-04-18", "The Fillmore", 42, venue1)));
		}
		
		@Test
		public void shouldProvideTheSecondGig() throws Exception {
			assertThat(gigs, hasItem(new Gig("Wild Flag", 1, "2012-04-19", "The Fillmore", 42, venue1)));
		}
	}
	
	public class WithManyGigsSameArtistSameDate {
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
						"        'venue':{'displayName':'The Fillmore','id':42}   " +
						"      },                                                 " +
						"      {                                                  " +
						"        'start':{'date':'2012-04-18'},                   " +
						"        'performance':[     							 " +
						"           {											 " +
						"			  'displayName': 'Wild Flag',     		     " +
						"             'artist': {'id': 1},     					 " +
						"           }     										 " +
						"        ],     											 " +
						"        'venue':{'displayName':'The Fillmore','id':42}   " +
						"      }                                                  " +
						"    ]}                                                   " +
						"  }                                                      " +
						"}  														 ";
		
		private List<Gig> gigs;
		private Venue venue1;
		
		@Before
		public void before() throws Exception {
			gigObservableFactory = new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0);
			venue1 = new Venue("name1", "postcode1", new Coordinate(51, 1));
			when(venueObservableFactory.create(42)).thenReturn(Observable.just(venue1));
			when(songKick.getGigs(0)).thenReturn(gigsJson);
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs.size(), is(1));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItem(new Gig("Wild Flag", 1, "2012-04-18", "The Fillmore", 42, venue1)));
		}
	}
	
	public class WithManyGigs {
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
				"        'venue':{'displayName':'The Fillmore','id':42}   " +
				"      },                                                 " +
				"      {                                                  " +
				"        'start':{'date':'2012-04-19'},                   " +
				"        'performance':[     							 " +
				"           {											 " +
				"			  'displayName': 'Steve Malkmus',     		 " +
				"             'artist': {'id': 2},     					 " +
				"           }     										 " +
				"        ],     											 " +
				"        'venue':{'displayName':'Bush Hall','id':43}      " +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
		
		private List<Gig> gigs;
		private Venue venue1;
		private Venue venue2;
		
		@Before
		public void before() throws Exception {
			gigObservableFactory = new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0);
			venue1 = new Venue("name1", "postcode1", new Coordinate(51, 1));
			venue2 = new Venue("name2", "postcode2", new Coordinate(51.1, 0));
			when(venueObservableFactory.create(42)).thenReturn(Observable.just(venue1));
			when(venueObservableFactory.create(43)).thenReturn(Observable.just(venue2));
			when(songKick.getGigs(0)).thenReturn(gigsJson);
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs.size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItem(new Gig("Wild Flag", 1, "2012-04-18", "The Fillmore", 42, venue1)));
		}
		
		@Test
		public void shouldProvideTheSecondGig() throws Exception {
			assertThat(gigs, hasItem(new Gig("Steve Malkmus", 2, "2012-04-19", "Bush Hall", 43, venue2)));
		}
	}
	
	public class WithManyPages {
		private static final String gigsJsonPage1 = 
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
				"        'venue':{'displayName':'The Fillmore','id':42}   " +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
		private static final String gigsJsonPage2 = 
				"{                                                        " +
				"  'resultsPage': {                                       " +
				"    'results': { 'event': [                              " +
				"      {                                                  " +
				"        'start':{'date':'2012-04-19'},                   " +
				"        'performance':[     							 " +
				"           {											 " +
				"			  'displayName': 'Steve Malkmus',     		 " +
				"             'artist': {'id': 2},     					 " +
				"           }     										 " +
				"        ],     											 " +
				"        'venue':{'displayName':'Bush Hall', 'id':43}	 " +
				"      }                                                  " +
				"    ]}                                                   " +
				"  }                                                      " +
				"}  														 ";
	                                                                                                                                  
		private List<Gig> gigs;
		private Venue venue1;
		private Venue venue2;
	        
		@Before
		public void before() throws Exception {
			gigObservableFactory = new GigObservableFactory(songKick, venueObservableFactory, 2, 100.0);
			venue1 = new Venue("name1", "postcode1", new Coordinate(51, -1));
			venue2 = new Venue("name2", "postcode2", new Coordinate(52, 0));
			when(venueObservableFactory.create(42)).thenReturn(Observable.just(venue1));
			when(venueObservableFactory.create(43)).thenReturn(Observable.just(venue2));
			when(songKick.getGigs(0)).thenReturn(gigsJsonPage1);
			when(songKick.getGigs(1)).thenReturn(gigsJsonPage2);
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs.size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItem(new Gig("Wild Flag", 1, "2012-04-18", "The Fillmore", 42, venue1)));
		}
		
		@Test
		public void shouldProvideTheSecondGig() throws Exception {
			assertThat(gigs, hasItem(new Gig("Steve Malkmus", 2, "2012-04-19", "Bush Hall", 43, venue2)));
		}
	}
}
