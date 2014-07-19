package com.artisan.radiohere;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import rx.Observable;
import rx.observers.TestObserver;

@RunWith(Nested.class)
public class GigFactoryTest {
	private SongKick songKick;
	private GigFactory gigObservableFactory;
	private TrackFactory trackObservableFactory;
	private JsonGigs jsonGigs;

	@Before
	public void before() {
		songKick = mock(SongKick.class);
		jsonGigs = mock(JsonGigs.class);
		trackObservableFactory = mock(TrackFactory.class);
		gigObservableFactory = spy(new GigFactory(trackObservableFactory, songKick, jsonGigs, 1, 100.0));
	}

	public class WithNoGigs {
		private List<Gig> gigs;
	        
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("{}");
			when(jsonGigs.extract("{}")).thenReturn(Observable.empty());
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
		private Gig gig = new Gig("artist", "date", "venueName", new Coordinate(51.5, -0.1));
	        
		@Before
		public void before() throws Exception {
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.just(gig));
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
		public void shouldProvideTheDate() throws Exception {
			assertThat(gigs.get(0).getDate(), equalTo("date"));
		}
		
		@Test
		public void shouldProvideTheVenueName() throws Exception {
			assertThat(gigs.get(0).getVenueName(), equalTo("venueName"));
		}

		@Test
		public void shouldProvideTheVenueDistance() throws Exception {
			assertThat(gigs.get(0).getVenueDistance(Coordinate.OLD_STREET), closeTo(3.185, 0.006));
		}
	}

	public class WithOneGigALongWayAway {
		private Gig gig = new Gig("artist", "date", "venueName", new Coordinate(12, 30));
		private List<Gig> gigs;
	        
		@Before
		public void before() throws Exception {
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.just(gig));
			Observable<Gig> observable = gigObservableFactory.create();
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithUnknownVenue {
		private Gig gig = new Gig("artist", "date", "venueName", null);
		private List<Gig> gigs;
		
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			Observable<Gig> observable = gigObservableFactory.create();
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.just(gig));
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithManyGigsSameArtistDifferentDate {
		private Gig gig1 = new Gig("artist", "date", "venueName", new Coordinate(51, 1));
		private Gig gig2 = new Gig("artist", "different date", "venueName", new Coordinate(51, 1));
		private List<Gig> gigs;
		
		@Before
		public void before() throws Exception {
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			Observable<Gig> observable = gigObservableFactory.create();
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.from(gig1, gig2));
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
		private Gig gig1 = new Gig("artist", "date", "venueName", new Coordinate(51, 1));
		private Gig gig2 = new Gig("artist", "date", "venueName", new Coordinate(51, 1));
		private List<Gig> gigs;
		
		@Before
		public void before() throws Exception {
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.from(gig1, gig2));
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
		private Gig gig1 = new Gig("artist1", "date", "venueName 1", new Coordinate(51, 1));
		private Gig gig2 = new Gig("artist2", "date", "venueName 2", new Coordinate(51.1, 0));
		private List<Gig> gigs;
		
		@Before
		public void before() throws Exception {
			when(trackObservableFactory.create("artist1")).thenReturn(Observable.empty());
			when(trackObservableFactory.create("artist2")).thenReturn(Observable.empty());
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.from(gig1, gig2));
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
		private Gig gig1 = new Gig("artist1", "date", "venueName 1", new Coordinate(51, -1));
		private Gig gig2 = new Gig("artist2", "date", "venueName 2", new Coordinate(52, 0));
		private List<Gig> gigs;
	        
		@Before
		public void before() throws Exception {
			when(trackObservableFactory.create("artist1")).thenReturn(Observable.empty());
			when(trackObservableFactory.create("artist2")).thenReturn(Observable.empty());
			gigObservableFactory = spy(new GigFactory(trackObservableFactory, songKick, jsonGigs, 2, 100.0));

			when(songKick.getGigs(0)).thenReturn("gigsJsonPage1");
			when(songKick.getGigs(1)).thenReturn("gigsJsonPage2");
			when(jsonGigs.extract("gigsJsonPage1")).thenReturn(Observable.just(gig1));
			when(jsonGigs.extract("gigsJsonPage2")).thenReturn(Observable.just(gig2));
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

	public class WithNoTracks {
		private List<Gig> gigs;
		private Gig gig = new Gig("artist", "date", "venueName", new Coordinate(51.5, -0.1));
	        
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.just(gig));
			Observable<Gig> observable = gigObservableFactory.create();
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			gigs = observable.toList().toBlockingObservable().single();	
		}
				
		@Test
		public void shouldHaveNoTracks() throws Exception {
			assertThat(gigs.get(0).getTracks(), empty());
		}
	}

	public class WithOneTrack {
		private Track track = new Track("name", "streamurl");
		private List<Gig> gigs;
		private Gig gig = new Gig("artist", "date", "venueName", new Coordinate(51.5, -0.1));
		
		@Before
		public void before() throws Exception {
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.just(gig));
			Observable<Gig> observable = gigObservableFactory.create();
			when(trackObservableFactory.create("artist")).thenReturn(Observable.just(track));
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldHaveOneTracks() throws Exception {
			assertThat(gigs.get(0).getTracks().size(), is(1));
		}

		@Test
		public void shouldHaveTheTrackDetails() throws Exception {
			assertThat(gigs.get(0).getTracks().get(0), equalTo(track));
		}
	}

	public class WithTwoTracks {
		private Track track1 = new Track("name1", "streamurl1");
		private Track track2 = new Track("name2", "streamurl2");
		private List<Gig> gigs;
		private Gig gig = new Gig("artist", "date", "venueName", new Coordinate(51.5, -0.1));
		
		@Before
		public void before() throws Exception {
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigs.extract("gigs Json")).thenReturn(Observable.just(gig));
			Observable<Gig> observable = gigObservableFactory.create();
			when(trackObservableFactory.create("artist")).thenReturn(Observable.from(track1, track2));
			gigs = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldHaveOneTracks() throws Exception {
			assertThat(gigs.get(0).getTracks().size(), is(2));
		}
		
		@Test
		public void shouldHaveTheTrackDetails() throws Exception {
			assertThat(gigs.get(0).getTracks(), contains(track1, track2));
		}
	}
}