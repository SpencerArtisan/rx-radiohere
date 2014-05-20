package radiohere;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;

@RunWith(Nested.class)
public class TrackObservableFactoryTest {
	public class WithNoTracks {
		private static final String tracksJson = "[]";
	                                                                                                                                  
		private List<Track> tracks;
	        
		@Before
		public void before() throws Exception {
			SoundCloud soundCloud = mock(SoundCloud.class);
			when(soundCloud.getTracks("Kate Denny")).thenReturn(tracksJson);
			when(soundCloud.getClientId()).thenReturn("CLIENT_ID");
			TrackObservableFactory factory = new TrackObservableFactory(soundCloud, 99);
			Observable<Track> observable = factory.create("Kate Denny");
			tracks = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveNoTracks() throws Exception {
			assertThat(tracks, empty());
		}
	}
	
	public class WithOneTrackWithoutStreamUrl {
		private static final String tracksJson = 
				"[											" +
				"	{										" +
				"		'title':'Closer to home',			" +
				"	}										" + 
				"]											";
		
		private List<Track> tracks;
		
		@Before
		public void before() throws Exception {
			SoundCloud soundCloud = mock(SoundCloud.class);
			when(soundCloud.getTracks("Kate Denny")).thenReturn(tracksJson);
			when(soundCloud.getClientId()).thenReturn("CLIENT_ID");
			TrackObservableFactory factory = new TrackObservableFactory(soundCloud, 99);
			Observable<Track> observable = factory.create("Kate Denny");
			tracks = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneTrack() throws Exception {
			assertThat(tracks, empty());
		}
	}
	
	public class WithOneTrack {
		private static final String tracksJson = 
				"[																		" +
				"	{																	" +
				"		'title':'Closer to home',			" +
				"		'stream_url':'http://api.soundcloud.com/tracks/52641865/stream',	" +
				"	}																	" + 
				"]																		";
		
		private List<Track> tracks;
		
		@Before
		public void before() throws Exception {
			SoundCloud soundCloud = mock(SoundCloud.class);
			when(soundCloud.getTracks("Kate Denny")).thenReturn(tracksJson);
			when(soundCloud.getClientId()).thenReturn("CLIENT_ID");
			TrackObservableFactory factory = new TrackObservableFactory(soundCloud, 99);
			Observable<Track> observable = factory.create("Kate Denny");
			tracks = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneTrack() throws Exception {
			assertThat(tracks.size(), is(1));
		}
		
		@Test
		public void shouldProvideTheTrack() throws Exception {
			assertThat(tracks.get(0), equalTo(
					new Track("Closer to home", "http://api.soundcloud.com/tracks/52641865/stream?client_id=CLIENT_ID")));
		}
	}
	
	public class WithManyTracks {
		private static final String tracksJson = 
				"[																		" +
				"	{																	" +
				"		'title':'Closer to home',			" +
				"		'stream_url':'http://api.soundcloud.com/tracks/52641865/stream',	" +
				"	},																	" + 
				"	{																	" +
				"		'title':'William and The Boat',									" +
				"		'stream_url':'http://api.soundcloud.com/tracks/25533842/stream',	" +
				"	}																	" + 
				"]																		";
		
		private List<Track> tracks;
		
		@Before
		public void before() throws Exception {
			SoundCloud soundCloud = mock(SoundCloud.class);
			when(soundCloud.getTracks("Kate Denny")).thenReturn(tracksJson);
			when(soundCloud.getClientId()).thenReturn("CLIENT_ID");
			TrackObservableFactory factory = new TrackObservableFactory(soundCloud, 99);
			Observable<Track> observable = factory.create("Kate Denny");
			tracks = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoTracks() throws Exception {
			assertThat(tracks.size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstTrack() throws Exception {
			assertThat(tracks.get(0), equalTo(
					new Track("Closer to home", "http://api.soundcloud.com/tracks/52641865/stream?client_id=CLIENT_ID")));
		}
		
		@Test
		public void shouldProvideTheSecondTrack() throws Exception {
			assertThat(tracks.get(1), equalTo(
					new Track("William and The Boat", "http://api.soundcloud.com/tracks/25533842/stream?client_id=CLIENT_ID")));
		}
	}
	
	public class WithMoreTracksThanRequired {
		private static final String tracksJson = 
				"[																		" +
						"	{																	" +
						"		'title':'Closer to home',			" +
						"		'stream_url':'http://api.soundcloud.com/tracks/52641865/stream',	" +
						"	},																	" + 
						"	{																	" +
						"		'title':'William and The Boat',									" +
						"		'stream_url':'http://api.soundcloud.com/tracks/25533842/stream',	" +
						"	}																	" + 
						"]																		";
		
		private List<Track> tracks;
		
		@Before
		public void before() throws Exception {
			SoundCloud soundCloud = mock(SoundCloud.class);
			when(soundCloud.getTracks("Kate Denny")).thenReturn(tracksJson);
			when(soundCloud.getClientId()).thenReturn("CLIENT_ID");
			TrackObservableFactory factory = new TrackObservableFactory(soundCloud, 1);
			Observable<Track> observable = factory.create("Kate Denny");
			tracks = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneTrack() throws Exception {
			assertThat(tracks.size(), is(1));
		}
		
		@Test
		public void shouldProvideTheFirstTrack() throws Exception {
			assertThat(tracks.get(0), equalTo(
					new Track("Closer to home", "http://api.soundcloud.com/tracks/52641865/stream?client_id=CLIENT_ID")));
		}
	}
}
