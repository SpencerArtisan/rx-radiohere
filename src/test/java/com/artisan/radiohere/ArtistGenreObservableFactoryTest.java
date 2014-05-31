package com.artisan.radiohere;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;

@RunWith(Nested.class)
public class ArtistGenreObservableFactoryTest {
	public class WhenArtistFound {
		private static final String artistGenreJson = 
		"	{                                         " +
		"	  'response': {                           " +
		"	    'artist': {                           " +
		"	      'artistGenres': [                         " +
		"	        { 'name': 'neo-psychedelic'},     " +
		"	        { 'name': 'space rock'},          " +
		"	      ],                                  " +
		"	      'id': 'AR2B0DQ1187B99B220',         " +
		"	      'name': 'Wooden Shjips'             " +
		"	    }                                     " +
		"	  }                                       " +
		"	}		                                  ";
		
		private ArtistGenre artistGenre;
	        
		@Before
		public void before() throws Exception {
			EchoNest echoNest = mock(EchoNest.class);
			when(echoNest.getArtist(42)).thenReturn(artistGenreJson);
			ArtistGenreObservableFactory factory = new ArtistGenreObservableFactory(echoNest);
			Observable<ArtistGenre> observable = factory.create(42);
			artistGenre = observable.toBlockingObservable().single();	
		}
		
		@Test
		public void shouldObserveTwoGenres() throws Exception {
			assertThat(artistGenre.getGenres().size(), is(2));
		}
		
		@Test
		public void shouldProvideTheFirstGenre() throws Exception {
			assertThat(artistGenre.getGenres(), hasItem("neo-psychedelic"));
		}
		
		@Test
		public void shouldProvideTheSecondGenre() throws Exception {
			assertThat(artistGenre.getGenres(), hasItem("space rock"));
		}
	}
}
