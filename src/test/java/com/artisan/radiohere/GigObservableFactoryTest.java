package com.artisan.radiohere;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;
import rx.observers.TestObserver;

@RunWith(Nested.class)
public class GigObservableFactoryTest {
	private SongKick songKick;
	private GigObservableFactory gigObservableFactory;
	private VenueObservableFactory venueObservableFactory;

	@Before
	public void before() {
		songKick = mock(SongKick.class);
		venueObservableFactory = mock(VenueObservableFactory.class);
		gigObservableFactory = spy(new GigObservableFactory(songKick, venueObservableFactory, 1, 100.0));
	}

	public class WithNoGigs {
		private List<Gig> gigs;
	        
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("{}");
			doReturn(Observable.empty()).when(gigObservableFactory).songKickToGigs("{}");
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
			when(songKick.getGigs(0)).thenThrow(new ApiException("", new Throwable()));
			observable = gigObservableFactory.create();
		}
		
		@Test
		public void shouldAbortTheObservable() throws Exception {
			TestObserver<Gig> testObserver = new TestObserver<>();
			observable.subscribe(testObserver);
			assertThat(testObserver.getOnNextEvents(), empty());
			assertThat(testObserver.getOnCompletedEvents(), empty());
			assertThat(testObserver.getOnErrorEvents().size(), is(1));
		}
	}
	
	public class WithOneGig {
		private List<Gig> gigs;
		private Venue venue;
		private Gig gig = new Gig("artist", 1, "date", "venueName", 2, null);
	        
		@Before
		public void before() throws Exception {
			venue = new Venue("name", "postcode", new Coordinate(51.5, -0.1));
			when(venueObservableFactory.create(2)).thenReturn(Observable.just(venue));
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			doReturn(Observable.just(gig)).when(gigObservableFactory).songKickToGigs("gigs Json");
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs.size(), is(1));
		}
		
		@Test
		public void shouldProvideTheArtist() throws Exception {
			assertThat(gigs.get(0).getArtist(), equalTo("artist"));
		}
		
		@Test
		public void shouldProvideTheArtistId() throws Exception {
			assertThat(gigs.get(0).getSongkickArtistId(), equalTo(1));
		}

		@Test
		public void shouldProvideTheDate() throws Exception {
			assertThat(gigs.get(0).getDate(), equalTo("date"));
		}
		
		@Test
		public void shouldProvideTheVenueName() throws Exception {
			assertThat(gigs.get(0).getVenueName(), equalTo("venueName"));
		}

		@Test
		public void shouldProvideTheVenueId() throws Exception {
			assertThat(gigs.get(0).getVenueId(), equalTo(2));
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
		private Gig gig = new Gig("artist", 1, "date", "venueName", 2, null);
		private List<Gig> gigs;
		private Venue venue;
	        
		@Before
		public void before() throws Exception {
			venue = new Venue("name", "postcode", new Coordinate(12, 30));
			when(venueObservableFactory.create(2)).thenReturn(Observable.just(venue));
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			doReturn(Observable.just(gig)).when(gigObservableFactory).songKickToGigs("gigs Json");
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithUnknownVenue {
		private Gig gig = new Gig("artist", 1, "date", "venueName", null, null);
		private List<Gig> gigs;
		
		@Before
		public void before() throws Exception {
			when(venueObservableFactory.create(null)).thenReturn(Observable.empty());
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			Observable<Gig> observable = gigObservableFactory.create();
			doReturn(Observable.just(gig)).when(gigObservableFactory).songKickToGigs("gigs Json");
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithManyGigsSameArtistDifferentDate {
		private Gig gig1 = new Gig("artist", 1, "date", "venueName", 2, null);
		private Gig gig2 = new Gig("artist", 1, "different date", "venueName", 2, null);
		private List<Gig> gigs;
		private Venue venue1;
		
		@Before
		public void before() throws Exception {
			venue1 = new Venue("name1", "postcode1", new Coordinate(51, 1));
			when(venueObservableFactory.create(2)).thenReturn(Observable.just(venue1));
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			Observable<Gig> observable = gigObservableFactory.create();
			doReturn(Observable.from(gig1, gig2)).when(gigObservableFactory).songKickToGigs("gigs Json");
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs.size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItem(gig1));
		}
		
		@Test
		public void shouldProvideTheSecondGig() throws Exception {
			assertThat(gigs, hasItem(gig2));
		}
	}
	
	public class WithManyGigsSameArtistSameDate {
		private Gig gig1 = new Gig("artist", 1, "date", "venueName", 2, null);
		private Gig gig2 = new Gig("artist", 1, "date", "venueName", 2, null);
		private List<Gig> gigs;
		private Venue venue1;
		
		@Before
		public void before() throws Exception {
			venue1 = new Venue("name1", "postcode1", new Coordinate(51, 1));
			when(venueObservableFactory.create(2)).thenReturn(Observable.just(venue1));
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			doReturn(Observable.from(gig1, gig2)).when(gigObservableFactory).songKickToGigs("gigs Json");
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs.size(), is(1));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItem(gig1));
		}
	}
	
	public class WithManyGigs {
		private Gig gig1 = new Gig("artist1", 1, "date", "venueName 1", 2, null);
		private Gig gig2 = new Gig("artist2", 1, "date", "venueName 2", 3, null);
		private List<Gig> gigs;
		private Venue venue1;
		private Venue venue2;
		
		@Before
		public void before() throws Exception {
			venue1 = new Venue("name1", "postcode1", new Coordinate(51, 1));
			venue2 = new Venue("name2", "postcode2", new Coordinate(51.1, 0));
			when(venueObservableFactory.create(2)).thenReturn(Observable.just(venue1));
			when(venueObservableFactory.create(3)).thenReturn(Observable.just(venue2));
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			doReturn(Observable.from(gig1, gig2)).when(gigObservableFactory).songKickToGigs("gigs Json");
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs.size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItem(gig1));
		}
		
		@Test
		public void shouldProvideTheSecondGig() throws Exception {
			assertThat(gigs, hasItem(gig2));
		}
	}
	
	public class WithManyPages {
		private Gig gig1 = new Gig("artist1", 1, "date", "venueName 1", 2, null);
		private Gig gig2 = new Gig("artist2", 1, "date", "venueName 2", 3, null);
		private List<Gig> gigs;
		private Venue venue1;
		private Venue venue2;
	        
		@Before
		public void before() throws Exception {
			gigObservableFactory = spy(new GigObservableFactory(songKick, venueObservableFactory, 2, 100.0));

			venue1 = new Venue("name1", "postcode1", new Coordinate(51, -1));
			venue2 = new Venue("name2", "postcode2", new Coordinate(52, 0));
			when(venueObservableFactory.create(2)).thenReturn(Observable.just(venue1));
			when(venueObservableFactory.create(3)).thenReturn(Observable.just(venue2));
			when(songKick.getGigs(0)).thenReturn("gigsJsonPage1");
			when(songKick.getGigs(1)).thenReturn("gigsJsonPage2");
			doReturn(Observable.just(gig1)).when(gigObservableFactory).songKickToGigs("gigsJsonPage1");
			doReturn(Observable.just(gig2)).when(gigObservableFactory).songKickToGigs("gigsJsonPage2");
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs.size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItem(gig1));
		}
		
		@Test
		public void shouldProvideTheSecondGig() throws Exception {
			assertThat(gigs, hasItem(gig2));
		}
	}
}
