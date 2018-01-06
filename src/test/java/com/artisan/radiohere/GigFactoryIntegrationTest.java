package com.artisan.radiohere;

import org.junit.Test;

import rx.Observable;

public class GigFactoryIntegrationTest {
	@Test
	public void dumpGigs() throws Exception {
		Observable<Gig> gigObservable = new GigFactory().create(new Coordinate(51.5403, -0.0884), 3.0);
		Dumper.dumpForTimePeriod(gigObservable, 12000);
	}
}
