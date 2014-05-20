package radiohere;

import static org.hamcrest.CoreMatchers.equalTo;
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
	public class WithOneTrack {
		private static final String tracksJson = 
				"[																		" +
				"	{																	" +
				"		'title':'kate denny - closer to home (album preview)',			" +
				"		'stream_url':'http://api.soundcloud.com/tracks/52641865/stream',	" +
				"	}																	" + 
				"]																		";
	                                                                                                                                  
		private List<Track> tracks;
	        
		@Before
		public void before() throws IOException {
			SoundCloud soundCloud = mock(SoundCloud.class);
			when(soundCloud.getTracks("Kate Denny")).thenReturn(tracksJson);
			when(soundCloud.getClientId()).thenReturn("CLIENT_ID");
			TrackObservableFactory factory = new TrackObservableFactory(soundCloud);
			Observable<Track> observable = factory.create("Kate Denny");
			tracks = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveOneTrack() throws Exception {
			assertThat(tracks.size(), is(1));
		}
		
		@Test
		public void shouldProvideTheName() throws Exception {
			assertThat(tracks.get(0).getName(), equalTo("kate denny - closer to home (album preview)"));
		}
		
		@Test
		public void shouldProvideTheStreamUrl() throws Exception {
			assertThat(tracks.get(0).getStreamUrl(), equalTo("http://api.soundcloud.com/tracks/52641865/stream?client_id=CLIENT_ID"));
		}
	}
	
	public class WithManyTracks {
		private static final String tracksJson = 
				"[																		" +
				"	{																	" +
				"		'title':'kate denny - closer to home (album preview)',			" +
				"		'stream_url':'http://api.soundcloud.com/tracks/52641865/stream',	" +
				"	},																	" + 
				"	{																	" +
				"		'title':'William and The Boat',									" +
				"		'stream_url':'http://api.soundcloud.com/tracks/25533842/stream',	" +
				"	}																	" + 
				"]																		";
		
		private List<Track> tracks;
		
		@Before
		public void before() throws IOException {
			SoundCloud soundCloud = mock(SoundCloud.class);
			when(soundCloud.getTracks("Kate Denny")).thenReturn(tracksJson);
			when(soundCloud.getClientId()).thenReturn("CLIENT_ID");
			TrackObservableFactory factory = new TrackObservableFactory(soundCloud);
			Observable<Track> observable = factory.create("Kate Denny");
			tracks = observable.toList().toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoTracks() throws Exception {
			assertThat(tracks.size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstTrackName() throws Exception {
			assertThat(tracks.get(0).getName(), equalTo("kate denny - closer to home (album preview)"));
		}
		
		@Test
		public void shouldProvideTheFirstTrackStreamUrl() throws Exception {
			assertThat(tracks.get(0).getStreamUrl(), equalTo("http://api.soundcloud.com/tracks/52641865/stream?client_id=CLIENT_ID"));
		}
		
		@Test
		public void shouldProvideTheSecondTrackName() throws Exception {
			assertThat(tracks.get(1).getName(), equalTo("William and The Boat"));
		}
		
		@Test
		public void shouldProvideTheSecondTrackStreamUrl() throws Exception {
			assertThat(tracks.get(1).getStreamUrl(), equalTo("http://api.soundcloud.com/tracks/25533842/stream?client_id=CLIENT_ID"));
		}
	}
}
