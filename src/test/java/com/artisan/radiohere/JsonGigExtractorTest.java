package com.artisan.radiohere;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.*;
import org.junit.runner.RunWith;

@RunWith(Nested.class)
public class JsonGigExtractorTest {
	private List<Gig> gigs;
	private JsonGigExtractor extractor = new JsonGigExtractor();
	
	public class WithNoGigs {
		private static final String gigsJson = 
				"{                                                        				" +
				"  'resultsPage': {                                       				" +
				"    'results': { 'event': [] }                             				" +
				"  }                                                      				" +
				"}  														 				";
	                                                                                                                                  
		@Before
		public void before() throws Exception {
			gigs = extractor.extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithNoPerformances {
		private static final String gigsJson = 
				"{                                                        				" +
				"  'resultsPage': {                                       				" +
				"    'results': { 'event': [                              				" +
				"      {                                                  				" +
				"        'start':{'date':'2012-04-18'},                   				" +
				"        'performance':[],     							 				" +
				"        'uri': 'songkick url',     				 	 	 				" +
				"        'venue':{'displayName':'The Fillmore', 'lat': 51, 'lng': 1}   	" +
				"      }                                                  				" +
				"    ]}                                                   				" +
				"  }                                                      				" +
				"}  														 				";
	                                                                                                                                  
		@Before
		public void before() throws Exception {
			gigs = extractor.extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs, hasSize(1));
		}

		@Test
		public void shouldProvideANullArtist() throws Exception {
			assertThat(gigs.get(0).getArtist(), nullValue());
		}
	}
	
	public class WithOneGig {
		private static final String gigsJson = 
				"{                                                        			" +
				"  'resultsPage': {                                       			" +
				"    'results': { 'event': [                              			" +
				"      {                                                  			" +
				"        'start':{'date':'2012-04-18'},                   			" +
				"        'performance':[     							 			" +
				"           {											 			" +
				"			  'displayName': 'Wild Flag'     			 			" +
				"           }     										 			" +
				"        ],     											 			" +
				"        'uri': 'songkick url',     				 	 	 			" +
				"        'venue':{'displayName':'The Fillmore', 'lat': 52, 'lng': 1} " +
				"      }                                                  			" +
				"    ]}                                                   			" +
				"  }                                                      			" +
				"}  														 			";
	                                                                                                                                  
		@Before
		public void before() throws Exception {
			gigs = extractor.extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs, hasSize(1));
		}
		
		@Test
		public void shouldProvideTheArtist() throws Exception {
			assertThat(gigs.get(0).getArtist(), is("Wild Flag"));
		}
		
		@Test
		public void shouldProvideTheDate() throws Exception {
			assertThat(gigs.get(0).getDate(), is("2012-04-18"));
		}
		
		@Test
		public void shouldProvideTheVenueName() throws Exception {
			assertThat(gigs.get(0).getVenueName(), is("The Fillmore"));
		}
		
		@Test
		public void shouldProvideTheVenueLocation() throws Exception {
			assertThat(gigs.get(0).getVenue(), is(new Coordinate(52, 1)));
		}

		@Test
		public void shouldProvideTheSongkickUrl() throws Exception {
			assertThat(gigs.get(0).getSongkickUrl(), is("songkick url"));
		}
	}
	
	public class WithManyGigs {
		private static final String gigsJson = 
				"{                                                        			" +
				"  'resultsPage': {                                       			" +
				"    'results': { 'event': [                              			" +
				"      {                                                  			" +
				"        'start':{'date':'2012-04-18'},                   			" +
				"        'performance':[     							 			" +
				"           {											 			" +
				"			  'displayName': 'Wild Flag'     			 			" +
				"           }     										 			" +
				"        ],     											 			" +
				"        'uri': 'songkick url',     				 	 	 			" +
				"        'venue':{'displayName':'The Fillmore', 'lat': 52, 'lng': 1} " +
				"      },                                                  			" +
				"      {                                                  			" +
				"        'start':{'date':'2012-04-18'},                   			" +
				"        'performance':[     							 			" +
				"           {											 			" +
				"			  'displayName': 'Wild Flag'     			 			" +
				"           }     										 			" +
				"        ],     											 			" +
				"        'uri': 'songkick url',     				 	 	 			" +
				"        'venue':{'displayName':'The Fillmore', 'lat': 52, 'lng': 1} " +
				"      }                                                  			" +
				"    ]}                                                   			" +
				"  }                                                      			" +
				"}  														 			";
	                                                                                                                                  
		@Before
		public void before() throws Exception {
			gigs = extractor.extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs, hasSize(2));
		}
	}
	
	public class WithNullLatLong {
		private static final String gigsJson = 
				"{                                                        				" +
				"  'resultsPage': {                                       				" +
				"    'results': { 'event': [                              				" +
				"      {                                                  				" +
				"        'start':{'date':'2012-04-18'},                   				" +
				"        'performance':[     							 				" +
				"           {											 				" +
				"			  'displayName': 'Wild Flag'     			 				" +
				"           }     										 				" +
				"        ],     											 				" +
				"        'uri': 'songkick url',     				 	 	 				" +
				"        'venue':{'displayName':'The Fillmore', 'lat':null, 'lng':null}	" +
				"      }                                                  				" +
				"    ]}                                                   				" +
				"  }                                                      				" +
				"}  														 				";
		
		@Before
		public void before() throws Exception {
			gigs = extractor.extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs, hasSize(1));
		}

		@Test
		public void shouldProvideANullVenueLocation() throws Exception {
			assertThat(gigs.get(0).getVenue(), nullValue());
		}
	}
	
	public class WithoutLatLong {
		private static final String gigsJson = 
				"{                                                        				" +
				"  'resultsPage': {                                       				" +
				"    'results': { 'event': [                              				" +
				"      {                                                  				" +
				"        'start':{'date':'2012-04-18'},                   				" +
				"        'performance':[     							 				" +
				"           {											 				" +
				"			  'displayName': 'Wild Flag'     			 				" +
				"           }     										 				" +
				"        ],     											 				" +
				"        'uri': 'songkick url',     				 	 	 				" +
				"        'venue':{'displayName':'The Fillmore'}							" +
				"      }                                                  				" +
				"    ]}                                                   				" +
				"  }                                                      				" +
				"}  														 				";
		
		@Before
		public void before() throws Exception {
			gigs = extractor.extract(gigsJson).toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs, hasSize(1));
		}
		
		@Test
		public void shouldProvideANullLocation() throws Exception {
			assertThat(gigs.get(0).getVenue(), nullValue());
		}
	}
}
