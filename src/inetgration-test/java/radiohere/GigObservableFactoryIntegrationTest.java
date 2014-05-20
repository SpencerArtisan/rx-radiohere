package radiohere;

import org.junit.Test;

import rx.Observable;

public class GigObservableFactoryIntegrationTest {
	@Test
	public void dumpGigs() throws Exception {
		SongKick songKick = new SongKick();
		Observable<Gig> gigObservable = new GigObservableFactory(
				songKick, 
				new VenueObservableFactory(songKick), 
				40).create();
		Dumper.dumpForTimePeriod(gigObservable, 10000);
	}
}
