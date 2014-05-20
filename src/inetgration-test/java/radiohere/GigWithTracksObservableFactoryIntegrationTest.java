package radiohere;

import org.junit.Test;

import rx.Observable;

public class GigWithTracksObservableFactoryIntegrationTest {
	@Test
	public void dumpGigsWithTracks() throws Exception {
		SongKick songKick = new SongKick();
		TrackObservableFactory trackObservableFactory = 
				new TrackObservableFactory(new SoundCloud());
		VenueObservableFactory venueObservableFactory = new VenueObservableFactory(songKick);
		GigObservableFactory gigObservableFactory = 
				new GigObservableFactory(songKick, venueObservableFactory, 10);
		Observable<GigWithTracks> gigWithTracksObservable = 
				new GigWithTracksObservableFactory(gigObservableFactory, trackObservableFactory).create();
		Dumper.dumpForTimePeriod(gigWithTracksObservable, 40000);
	}
}
