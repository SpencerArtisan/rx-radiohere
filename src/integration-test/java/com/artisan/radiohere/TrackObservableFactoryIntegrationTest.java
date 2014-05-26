package com.artisan.radiohere;

import org.junit.Test;

import com.artisan.radiohere.SoundCloud;
import com.artisan.radiohere.Track;
import com.artisan.radiohere.TrackObservableFactory;

import rx.Observable;

public class TrackObservableFactoryIntegrationTest {
	@Test
	public void dumpTracks() throws Exception {
		Observable<Track> trackObservable = new TrackObservableFactory(new SoundCloud(), 20)
				.create("Stephen Malkmus");
		Dumper.dumpForTimePeriod(trackObservable, 2000);
	}
}
