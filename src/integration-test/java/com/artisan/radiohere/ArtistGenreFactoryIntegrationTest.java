package com.artisan.radiohere;

import org.junit.Test;

import rx.Observable;

public class ArtistGenreFactoryIntegrationTest {
	@Test
	public void dumpGenres() throws Exception {
		Observable<ArtistGenre> genreObservable 
				= new ArtistGenreObservableFactory().create(569763);
		Dumper.dumpForTimePeriod(genreObservable, 12000);
	}
}
