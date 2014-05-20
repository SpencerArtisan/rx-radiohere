package radiohere;

import org.junit.Test;

import rx.Observable;

public class TrackObservableFactoryIntegrationTest {
	@Test
	public void dumpTracks() throws Exception {
		Observable<Track> trackObservable = new TrackObservableFactory(new SoundCloud())
				.create("Stephen Malkmus");
		Dumper.dumpForTimePeriod(trackObservable, 1000);
	}
}
