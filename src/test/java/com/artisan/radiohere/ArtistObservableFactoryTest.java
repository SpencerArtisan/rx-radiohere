package com.artisan.radiohere;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;

@RunWith(Nested.class)
public class ArtistObservableFactoryTest {
	@Mock
	private GigObservableFactory gigObservableFactory;
	
	@Mock
	private TrackObservableFactory trackObservableFactory;

	private ArtistObservableFactory artistObservableFactory;
	private Observable<Artist> artistObservable;
	private List<Artist> artists;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		artistObservableFactory = new ArtistObservableFactory(
				gigObservableFactory, 
				trackObservableFactory);
	}
	
	public class WithNoGigs {
		@Before
		public void before() {
			when(gigObservableFactory.create()).thenReturn(Observable.empty());
			artistObservable = artistObservableFactory.create();
			artists = artistObservable.toList().toBlockingObservable().single();			
		}
		
		@Test
		public void shouldBeNoArtists() throws Exception {
			assertThat(artists, empty());
		}
	}
	
	public class WithOneGigAndNoTracks {
		private Gig gig;

		@Before
		public void before() {
			gig = new Gig("artist", 1, "date", "venue", 42, null);
			when(gigObservableFactory.create()).thenReturn(Observable.just(gig));
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			artistObservable = artistObservableFactory.create();			
			artists = artistObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeOneArtist() throws Exception {
			assertThat(artists.size(), is(1));
		}
		
		@Test
		public void shouldHaveTheGigDetails() throws Exception {
			assertThat(artists.get(0).getGig(), equalTo(gig));
		}
		
		@Test
		public void shouldHaveNoTracks() throws Exception {
			assertThat(artists.get(0).getTracks(), empty());
		}
	}

	public class WithOneGigAndOneTrack {
		private Gig gig;
		private Track track;
		
		@Before
		public void before() {
			gig = new Gig("artist", 1, "date", "venue", 42, null);
			track = new Track("name", "streamurl");
			when(gigObservableFactory.create()).thenReturn(Observable.just(gig));
			when(trackObservableFactory.create("artist")).thenReturn(Observable.just(track));
			artistObservable = artistObservableFactory.create();			
			artists = artistObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeOneArtist() throws Exception {
			assertThat(artists.size(), is(1));
		}
		
		@Test
		public void shouldBeOneTrack() throws Exception {
			assertThat(artists.get(0).getTracks().size(), is(1));
		}
		
		@Test
		public void shouldHaveTheGigDetails() throws Exception {
			assertThat(artists.get(0).getGig(), equalTo(gig));
		}
		
		@Test
		public void shouldHaveTheTrackDetails() throws Exception {
			assertThat(artists.get(0).getTracks().get(0), equalTo(track));
		}
	}
	
	public class WithOneGigAndTwoTracks {
		private Gig gig;
		private Track track1;
		private Track track2;
		
		@Before
		public void before() {
			gig = new Gig("artist", 1, "date", "venue", 42, null);
			track1 = new Track("name1", "streamurl1");
			track2 = new Track("name2", "streamurl2");
			when(gigObservableFactory.create()).thenReturn(Observable.just(gig));
			when(trackObservableFactory.create("artist")).thenReturn(Observable.from(track1, track2));
			artistObservable = artistObservableFactory.create();			
			artists = artistObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeOneArtist() throws Exception {
			assertThat(artists.size(), is(1));
		}
		
		@Test
		public void shouldBeTwoTracks() throws Exception {
			assertThat(artists.get(0).getTracks().size(), is(2));
		}
		
		@Test
		public void shouldHaveTheGigDetails() throws Exception {
			assertThat(artists.get(0).getGig(), equalTo(gig));
		}
		
		@Test
		public void shouldHaveTheTrackDetails() throws Exception {
			assertThat(artists.get(0).getTracks(), contains(track1, track2));
		}
	}
	
	public class WithTwoGigsFromSameArtistAndTwoTracks {
		private Gig gig1;
		private Gig gig2;
		private Track track1;
		private Track track2;
		
		@Before
		public void before() {
			gig1 = new Gig("artist", 1, "date1", "venue1", 42, null);
			gig2 = new Gig("artist", 1, "date2", "venue2", 43, null);
			track1 = new Track("name1", "streamurl1");
			track2 = new Track("name2", "streamurl2");
			when(gigObservableFactory.create()).thenReturn(Observable.from(gig1, gig2));
			when(trackObservableFactory.create("artist")).thenReturn(Observable.from(track1, track2));
			artistObservable = artistObservableFactory.create();			
			artists = artistObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeTwoArtists() throws Exception {
			assertThat(artists.size(), is(2));
		}
		
		@Test
		public void firstEmittedArtistShouldHaveTwoTracks() throws Exception {
			assertThat(artists.get(0).getTracks().size(), is(2));
		}
		
		@Test
		public void firstEmittedArtistShouldHaveTheGigDetails() throws Exception {
			assertThat(artists.get(0).getGig(), equalTo(gig1));
		}
		
		@Test
		public void firstEmittedArtistShouldHaveTheTrackDetails() throws Exception {
			assertThat(artists.get(0).getTracks(), contains(track1, track2));
		}
		
		@Test
		public void secondEmittedArtistShouldHaveTwoTracks() throws Exception {
			assertThat(artists.get(1).getTracks().size(), is(2));
		}
		
		@Test
		public void secondEmittedArtistShouldHaveTheGigDetails() throws Exception {
			assertThat(artists.get(1).getGig(), equalTo(gig2));
		}
		
		@Test
		public void secondEmittedArtistShouldHaveTheTrackDetails() throws Exception {
			assertThat(artists.get(1).getTracks(), contains(track1, track2));
		}
	}
	
	public class WithTwoGigsFromDifferentArtistsWithOneTrackEach {
		private Gig gig1;
		private Gig gig2;
		private Track track1;
		private Track track2;

		@Before
		public void before() {
			gig1 = new Gig("artist1", 1, "date1", "venue1", 42, null);
			gig2 = new Gig("artist2", 2, "date2", "venue2", 43, null);
			track1 = new Track("name1", "streamurl1");
			track2 = new Track("name2", "streamurl2");
			when(gigObservableFactory.create()).thenReturn(Observable.from(gig1, gig2));
			when(trackObservableFactory.create("artist1")).thenReturn(Observable.just(track1));
			when(trackObservableFactory.create("artist2")).thenReturn(Observable.just(track2));
			artistObservable = artistObservableFactory.create();			
			artists = artistObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeTwoArtists() throws Exception {
			assertThat(artists.size(), is(2));
		}
		
		@Test
		public void shouldBeOneTracksForFirstArtist() throws Exception {
			assertThat(artists.get(0).getTracks().size(), is(1));
		}
		
		@Test
		public void shouldBeOneTracksForSecondArtist() throws Exception {
			assertThat(artists.get(1).getTracks().size(), is(1));
		}
		
		@Test
		public void shouldHaveTheFirstArtistGigDetails() throws Exception {
			assertThat(artists.get(0).getGig(), equalTo(gig1));
		}
		
		@Test
		public void shouldHaveTheSecondArtistGigDetails() throws Exception {
			assertThat(artists.get(1).getGig(), equalTo(gig2));
		}
		
		@Test
		public void shouldHaveTheFirstArtistTrackDetails() throws Exception {
			assertThat(artists.get(0).getTracks().get(0), equalTo(track1));
		}
		
		@Test
		public void shouldHaveTheSecondArtistTrackDetails() throws Exception {
			assertThat(artists.get(1).getTracks().get(0), equalTo(track2));
		}
	}
}
