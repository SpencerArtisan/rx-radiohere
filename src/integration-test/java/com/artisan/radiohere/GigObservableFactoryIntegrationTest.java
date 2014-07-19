package com.artisan.radiohere;

import org.junit.Test;

import rx.Observable;

public class GigObservableFactoryIntegrationTest {
	@Test
	public void dumpGigs() throws Exception {
		Observable<Gig> gigObservable = new GigFactory().create();
		Dumper.dumpForTimePeriod(gigObservable, 12000);
	}
}
