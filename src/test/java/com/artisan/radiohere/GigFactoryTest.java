package com.artisan.radiohere;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.*;
import org.junit.runner.RunWith;

import rx.Observable;
import rx.observers.TestObserver;

@RunWith(Nested.class)
public class GigFactoryTest {
	private static final Coordinate YEATE_STREET = new Coordinate(51.5403, -0.0884);
	private SongKick songKick;
	private GigFactory gigFactory;
	private TrackFactory trackFactory;
	private JsonGigExtractor jsonGigExtractor;
	private List<Gig> gigs;

	@Before
	public void before() {
		songKick = mock(SongKick.class);
		jsonGigExtractor = mock(JsonGigExtractor.class);
		trackFactory = mock(TrackFactory.class);
		gigFactory = spy(new GigFactory(trackFactory, songKick, jsonGigExtractor, 1));
	}

	public class WithNoGigs {
		private List<Gig> gigs;
	        
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.empty());
			gigs = gigFactory.create(YEATE_STREET, 100.0).toList().toBlockingObservable().single();	
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
			observable = gigFactory.create(YEATE_STREET, 10.0);
		}
		
		@Test
		public void shouldAbortTheObservable() throws Exception {
			TestObserver<Gig> testObserver = new TestObserver<>();
			observable.subscribe(testObserver);
			assertThat(testObserver.getOnNextEvents(), empty());
			assertThat(testObserver.getOnCompletedEvents(), empty());
			assertThat(testObserver.getOnErrorEvents(), hasSize(1));
		}
	}
	
	public class WithOneGig {
		private Gig gig = new Gig("artist", "date", "venueName", YEATE_STREET, "songkickUrl");
	        
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.just(gig));
			when(trackFactory.create("artist")).thenReturn(Observable.empty());
			gigs = gigFactory.create(YEATE_STREET, 100.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs, hasSize(1));
		}
		
		@Test
		public void shouldProvideTheArtist() throws Exception {
			assertThat(gigs.get(0).getArtist(), is("artist"));
		}

		@Test
		public void shouldProvideTheDate() throws Exception {
			assertThat(gigs.get(0).getDate(), is("date"));
		}
		
		@Test
		public void shouldProvideTheVenueName() throws Exception {
			assertThat(gigs.get(0).getVenueName(), is("venueName"));
		}

		@Test
		public void shouldProvideTheSongkickUrl() throws Exception {
			assertThat(gigs.get(0).getSongkickUrl(), is("songkickUrl"));
		}
	}

	public class WithOneGigALongWayAway {
		private Gig gig = new Gig("artist", "date", "venueName", new Coordinate(-1, -1), "songkickUrl");
	        
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.just(gig));
			when(trackFactory.create("artist")).thenReturn(Observable.empty());
			gigs = gigFactory.create(YEATE_STREET, 100.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithUnknownVenue {
		private Gig gig = new Gig("artist", "date", "venueName", null, "songkickUrl");
		
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.just(gig));
			gigs = gigFactory.create(YEATE_STREET, 100.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoGigs() throws Exception {
			assertThat(gigs, empty());
		}
	}
	
	public class WithManyGigsSameArtistDifferentDate {
		private Gig gig1 = new Gig("artist", "date", "venueName", YEATE_STREET, "songkickUrl");
		private Gig gig2 = new Gig("artist", "different date", "venueName", YEATE_STREET, "songkickUrl");
		
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.from(gig1, gig2));
			when(trackFactory.create("artist")).thenReturn(Observable.empty());
			gigs = gigFactory.create(YEATE_STREET, 100.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs, hasSize(2));
		}
		
		@Test
		public void shouldProvideTheGigs() throws Exception {
			assertThat(gigs, hasItems(gig1, gig2));
		}
	}
	
	public class WithManyGigsSameArtistSameDate {
		private Gig gig1 = new Gig("artist", "date", "venueName", YEATE_STREET, "songkickUrl");
		private Gig gig2 = new Gig("artist", "date", "venueName", YEATE_STREET, "songkickUrl");
		
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.from(gig1, gig2));
			when(trackFactory.create("artist")).thenReturn(Observable.empty());
			gigs = gigFactory.create(YEATE_STREET, 100.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneGig() throws Exception {
			assertThat(gigs, hasSize(1));
		}
		
		@Test
		public void shouldProvideTheFirstGig() throws Exception {
			assertThat(gigs, hasItems(gig1));
		}
	}
	
	public class WithManyGigs {
		private Gig gig1 = new Gig("artist1", "date", "venueName 1", YEATE_STREET, "songkickUrl");
		private Gig gig2 = new Gig("artist2", "date", "venueName 2", YEATE_STREET, "songkickUrl");
		
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.from(gig1, gig2));
			when(trackFactory.create("artist1")).thenReturn(Observable.empty());
			when(trackFactory.create("artist2")).thenReturn(Observable.empty());
			gigs = gigFactory.create(YEATE_STREET, 100.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs, hasSize(2));
		}
		
		@Test
		public void shouldProvideTheGigs() throws Exception {
			assertThat(gigs, hasItems(gig1, gig2));
		}
	}
	
	public class WithManyPages {
		private Gig gig1 = new Gig("artist1", "date", "venueName 1", YEATE_STREET, "songkickUrl");
		private Gig gig2 = new Gig("artist2", "date", "venueName 2", YEATE_STREET, "songkickUrl");
	        
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigsJsonPage1");
			when(songKick.getGigs(1)).thenReturn("gigsJsonPage2");
			when(jsonGigExtractor.extract("gigsJsonPage1")).thenReturn(Observable.just(gig1));
			when(jsonGigExtractor.extract("gigsJsonPage2")).thenReturn(Observable.just(gig2));
			when(trackFactory.create("artist1")).thenReturn(Observable.empty());
			when(trackFactory.create("artist2")).thenReturn(Observable.empty());
			gigFactory = new GigFactory(trackFactory, songKick, jsonGigExtractor, 2);
			gigs = gigFactory.create(YEATE_STREET, 100.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGigs() throws Exception {
			assertThat(gigs, hasSize(2));
		}
		
		@Test
		public void shouldProvideTheGigs() throws Exception {
			assertThat(gigs, hasItems(gig1, gig2));
		}
	}

	public class WithNoTracks {
		private Gig gig = new Gig("artist", "date", "venueName", YEATE_STREET, "songkickUrl");
	        
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.just(gig));
			when(trackFactory.create("artist")).thenReturn(Observable.empty());
			gigs = gigFactory.create(YEATE_STREET, 10.0).toList().toBlockingObservable().single();	
		}
				
		@Test
		public void shouldHaveNoTracks() throws Exception {
			assertThat(gigs.get(0).getTracks(), empty());
		}
	}

	public class WithOneTrack {
		private Track track = new Track("name", "streamurl");
		private Gig gig = new Gig("artist", "date", "venueName", YEATE_STREET, "songkickUrl");
		
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.just(gig));
			when(trackFactory.create("artist")).thenReturn(Observable.just(track));
			gigs = gigFactory.create(YEATE_STREET, 10.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldHaveOneTracks() throws Exception {
			assertThat(gigs.get(0).getTracks(), hasSize(1));
		}

		@Test
		public void shouldHaveTheTrackDetails() throws Exception {
			assertThat(gigs.get(0).getTracks(), hasItems(track));
		}
	}

	public class WithTwoTracks {
		private Track track1 = new Track("name1", "streamurl1");
		private Track track2 = new Track("name2", "streamurl2");
		private Gig gig = new Gig("artist", "date", "venueName", YEATE_STREET, "songkickUrl");
		
		@Before
		public void before() throws Exception {
			when(songKick.getGigs(0)).thenReturn("gigs Json");
			when(jsonGigExtractor.extract("gigs Json")).thenReturn(Observable.just(gig));
			when(trackFactory.create("artist")).thenReturn(Observable.from(track1, track2));
			gigs = gigFactory.create(YEATE_STREET, 10.0).toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldHaveOneTracks() throws Exception {
			assertThat(gigs.get(0).getTracks(), hasSize(2));
		}
		
		@Test
		public void shouldHaveTheTrackDetails() throws Exception {
			assertThat(gigs.get(0).getTracks(), hasItems(track1, track2));
		}
	}
}
