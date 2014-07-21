package com.artisan.radiohere;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.*;
import org.junit.runner.RunWith;

@RunWith(Nested.class)
public class JsonTrackExtractorTest {
	private List<Track> tracks;
	private JsonTrackExtractor extractor = new JsonTrackExtractor();
	
	public class WithNoTracks {
		private static final String tracksJson = "[]";
	        
		@Before
		public void before() throws Exception {
			tracks = extractor.extract(tracksJson, "CLIENT_ID").toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldObserveNoTracks() throws Exception {
			assertThat(tracks, empty());
		}
	}
	
	public class WithOneTrackWithoutStreamUrl {
		private static final String tracksJson = 
				"[								" +
				"	{							" +
				"		'title':'a title',		" +
				"	}							" + 
				"]								";
		
		@Before
		public void before() throws Exception {
			tracks = extractor.extract(tracksJson, "CLIENT_ID").toList().toBlockingObservable().single();
		}

		@Test
		public void shouldObserveOneTrack() throws Exception {
			assertThat(tracks, hasSize(1));
		}

		@Test
		public void shouldProvideANullStreamUrl() throws Exception {
			assertThat(tracks.get(0).getStreamUrl(), nullValue());
		}
	}
	
	public class WithOneTrack {
		private static final String tracksJson = 
				"[								" +
				"	{							" +
				"		'title':'a title',		" +
				"		'stream_url':'a url',	" +
				"	}							" + 
				"]								";
		
		@Before
		public void before() throws Exception {
			tracks = extractor.extract(tracksJson, "CLIENT_ID").toList().toBlockingObservable().single();
		}

		@Test
		public void shouldObserveOneTrack() throws Exception {
			assertThat(tracks, hasSize(1));
		}

		@Test
		public void shouldProvideATitle() throws Exception {
			assertThat(tracks.get(0).getName(), is("a title"));
		}
		
		@Test
		public void shouldProvideAStreamUrl() throws Exception {
			assertThat(tracks.get(0).getStreamUrl(), is("a url?client_id=CLIENT_ID"));
		}
	}
	
	public class WithManyTracks {
		private static final String tracksJson = 
				"[								" +
				"	{							" +
				"		'title':'title 1',		" +
				"		'stream_url':'url 1'		" +
				"	},							" + 
				"	{							" +
				"		'title':'title 2',		" +
				"		'stream_url':'url 2'		" +
				"	}							" + 
				"]								";
		
		@Before
		public void before() throws Exception {
			tracks = extractor.extract(tracksJson, "CLIENT_ID").toList().toBlockingObservable().single();
		}

		@Test
		public void shouldObserveTwoTracks() throws Exception {
			assertThat(tracks, hasSize(2));
		}

		@Test
		public void shouldProvideTheFirstTrackTitle() throws Exception {
			assertThat(tracks.get(0).getName(), is("title 1"));
		}
		
		@Test
		public void shouldProvideTheFirstTrackStreamUrl() throws Exception {
			assertThat(tracks.get(0).getStreamUrl(), is("url 1?client_id=CLIENT_ID"));
		}
		
		@Test
		public void shouldProvideTheSecondTrackTitle() throws Exception {
			assertThat(tracks.get(1).getName(), is("title 2"));
		}
		
		@Test
		public void shouldProvideTheSecondTrackStreamUrl() throws Exception {
			assertThat(tracks.get(1).getStreamUrl(), is("url 2?client_id=CLIENT_ID"));
		}
	}
}
