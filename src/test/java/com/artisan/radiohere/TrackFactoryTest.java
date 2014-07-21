package com.artisan.radiohere;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.*;
import org.junit.runner.RunWith;

import rx.Observable;

@RunWith(Nested.class)
public class TrackFactoryTest {
	private List<Track> tracks;
	private SoundCloud soundCloud;
	private TrackFactory factory;
	private JsonTrackExtractor jsonTrackExtractor;
	
	@Before
	public void before() throws Exception {
		soundCloud = mock(SoundCloud.class);
		when(soundCloud.getClientId()).thenReturn("CLIENT_ID");
		jsonTrackExtractor = mock(JsonTrackExtractor.class);
		factory = new TrackFactory(soundCloud, jsonTrackExtractor, 99);
	}

	public class WithNoTracks {
		@Before
		public void before() throws Exception {
			when(soundCloud.getTracks("Kate Denny")).thenReturn("gigs Json");
			when(jsonTrackExtractor.extract("gigs Json", "CLIENT_ID")).thenReturn(Observable.empty());
			tracks = factory.create("Kate Denny").toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoTracks() throws Exception {
			assertThat(tracks, empty());
		}
	}
	
	public class WithOneTrackWithoutStreamUrl {
		private Track track = new Track("name", null);
		
		@Before
		public void before() throws Exception {
			when(soundCloud.getTracks("Kate Denny")).thenReturn("gigs Json");
			when(jsonTrackExtractor.extract("gigs Json", "CLIENT_ID")).thenReturn(Observable.just(track));
			tracks = factory.create("Kate Denny").toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneTrack() throws Exception {
			assertThat(tracks, empty());
		}
	}
	
	public class WithOneTrack {
		private Track track = new Track("name", "stream url");
		
		@Before
		public void before() throws Exception {
			when(soundCloud.getTracks("Kate Denny")).thenReturn("gigs Json");
			when(jsonTrackExtractor.extract("gigs Json", "CLIENT_ID")).thenReturn(Observable.just(track));
			tracks = factory.create("Kate Denny").toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneTrack() throws Exception {
			assertThat(tracks, hasSize(1));
		}
		
		@Test
		public void shouldProvideTheTrack() throws Exception {
			assertThat(tracks, hasItems(track));
		}
	}
	
	public class WithManyTracks {
		private Track track1 = new Track("name1", "stream url1");
		private Track track2 = new Track("name2", "stream url2");
		
		@Before
		public void before() throws Exception {
			when(soundCloud.getTracks("Kate Denny")).thenReturn("gigs Json");
			when(jsonTrackExtractor.extract("gigs Json", "CLIENT_ID")).thenReturn(Observable.from(track1, track2));
			tracks = factory.create("Kate Denny").toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoTracks() throws Exception {
			assertThat(tracks, hasSize(2));
		}
		
		@Test
		public void shouldProvideTheTracks() throws Exception {
			assertThat(tracks, hasItems(track1, track2));
		}
	}
	
	public class WithMoreTracksThanRequired {
		private Track track1 = new Track("name1", "stream url1");
		private Track track2 = new Track("name2", "stream url2");
		
		@Before
		public void before() throws Exception {
			when(soundCloud.getTracks("Kate Denny")).thenReturn("gigs Json");
			when(jsonTrackExtractor.extract("gigs Json", "CLIENT_ID")).thenReturn(Observable.from(track1, track2));
			factory = new TrackFactory(soundCloud, jsonTrackExtractor, 1);
			tracks = factory.create("Kate Denny").toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoTracks() throws Exception {
			assertThat(tracks, hasSize(1));
		}
		
		@Test
		public void shouldProvideTheTracks() throws Exception {
			assertThat(tracks, hasItems(track1));
		}
	}
}
