package radiohere;

import org.junit.Test;

import rx.Observable;

public class ArtistObservableFactoryIntegrationTest {
	@Test
	public void dump() throws Exception {
		SongKick songKick = new SongKick();
		TrackObservableFactory trackObservableFactory = 
				new TrackObservableFactory(new SoundCloud(), 4);
		VenueObservableFactory venueObservableFactory = new VenueObservableFactory(songKick);
		GigObservableFactory gigObservableFactory = 
				new GigObservableFactory(songKick, venueObservableFactory, 5, 5.0);
		Observable<Artist> artistObservable = 
				new ArtistObservableFactory(gigObservableFactory, trackObservableFactory).create();
		Dumper.dumpForTimePeriod(artistObservable, 2000);
	}
}
