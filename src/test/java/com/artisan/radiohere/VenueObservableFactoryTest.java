package com.artisan.radiohere;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;
import static org.hamcrest.Matchers.empty;

@RunWith(Nested.class)
public class VenueObservableFactoryTest {
	public class WithVenue {
		private static final String venueJson = 
		"	{                                             " +
		"    'resultsPage': {                             " +
		"      'results': {                               " +
		"        'venue': {                               " +
		"          'displayName':'O2 Academy Brixton',    " +
		"           'zip':'SW9 9SL',                      " +
		"           'lat':51.4651268, 'lng':-0.115187,    " +
		"        }                                        " +
		"      },                                         " +
		"    }                                            " +
		"  }	                                          	 ";                                                                                       
		private Venue venue;
	        
		@Before
		public void before() throws Exception {
			SongKick songKick = mock(SongKick.class);
			when(songKick.getVenue(42)).thenReturn(venueJson);
			VenueObservableFactory factory = new VenueObservableFactory(songKick);
			Observable<Venue> observable = factory.create(42);
			venue = observable.toBlockingObservable().single();	
		}
		
		@Test
		public void shouldProvideThePostcode() throws Exception {
			assertThat(venue.getPostcode(), equalTo("SW9 9SL"));
		}
		
		@Test
		public void shouldProvideTheName() throws Exception {
			assertThat(venue.getName(), equalTo("O2 Academy Brixton"));
		}

		@Test
		public void shouldProvideTheLocation() throws Exception {
			assertThat(venue.getCoordinate(), equalTo(new Coordinate(51.4651268, -0.115187)));
		}
	}

	public class WithoutLatLong {
		private static final String venueJson = 
				"	{                                             " +
						"    'resultsPage': {                             " +
						"      'results': {                               " +
						"        'venue': {                               " +
						"          'displayName':'O2 Academy Brixton',    " +
						"           'zip':'SW9 9SL'                      " +
						"        }                                        " +
						"      },                                         " +
						"    }                                            " +
						"  }	                                          	 ";                                                                                       
		private List<Venue> venues;
		
		@Before
		public void before() throws Exception {
			SongKick songKick = mock(SongKick.class);
			when(songKick.getVenue(42)).thenReturn(venueJson);
			VenueObservableFactory factory = new VenueObservableFactory(songKick);
			Observable<Venue> observable = factory.create(42);
			venues = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldNotProvideAVenue() throws Exception {
			assertThat(venues, empty());
		}
	}
}
