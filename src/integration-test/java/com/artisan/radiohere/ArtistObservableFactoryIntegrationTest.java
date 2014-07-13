package com.artisan.radiohere;

import org.junit.Test;

public class ArtistObservableFactoryIntegrationTest {
	@Test
	public void dump() throws Exception {
		Dumper.dumpForTimePeriod(new ArtistObservableFactory().create(), 12000);
	}
}
