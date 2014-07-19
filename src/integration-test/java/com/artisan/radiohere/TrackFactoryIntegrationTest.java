package com.artisan.radiohere;

import org.junit.Test;

import rx.Observable;

public class TrackFactoryIntegrationTest {
	@Test
	public void dumpTracks() throws Exception {
		Observable<Track> trackObservable = new TrackFactory(new SoundCloud(), 20)
				.create("Stephen Malkmus");
		Dumper.dumpForTimePeriod(trackObservable, 2000);
		Dumper.dumpForTimePeriod(trackObservable, 2000);
	}
}
