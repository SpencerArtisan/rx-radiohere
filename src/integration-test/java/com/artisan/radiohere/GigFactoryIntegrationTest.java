package com.artisan.radiohere;

import org.junit.Test;

import rx.Observable;

public class GigFactoryIntegrationTest {
	@Test
	public void dumpGigs() throws Exception {
		Observable<Gig> gigObservable = new GigFactory().create(Coordinate.YEATE_STREET, 3.0);
		Dumper.dumpForTimePeriod(gigObservable, 12000);
	}
}
