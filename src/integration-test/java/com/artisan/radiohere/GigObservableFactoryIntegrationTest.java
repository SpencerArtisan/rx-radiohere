package com.artisan.radiohere;

import org.junit.Test;

import com.artisan.radiohere.Gig;
import com.artisan.radiohere.GigObservableFactory;
import com.artisan.radiohere.SongKick;
import com.artisan.radiohere.VenueObservableFactory;

import rx.Observable;

public class GigObservableFactoryIntegrationTest {
	@Test
	public void dumpGigs() throws Exception {
		SongKick songKick = new SongKick();
		Observable<Gig> gigObservable = new GigObservableFactory(
				songKick, 
				new VenueObservableFactory(songKick), 
				40, 5).create();
		Dumper.dumpForTimePeriod(gigObservable, 10000);
	}
}
