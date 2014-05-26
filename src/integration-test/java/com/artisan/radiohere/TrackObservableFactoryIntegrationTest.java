package com.artisan.radiohere;

import org.junit.Test;

import rx.Observable;

public class TrackObservableFactoryIntegrationTest {
	@Test
	public void dumpTracks() throws Exception {
		Observable<Track> trackObservable = new TrackObservableFactory(new SoundCloud(), 20)
				.create("Stephen Malkmus");
		Dumper.dumpForTimePeriod(trackObservable, 2000);
	}
}
