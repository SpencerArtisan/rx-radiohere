package radiohere;

import rx.Observable;

public class GigWithTracksObservableFactory {
	private final GigObservableFactory gigObservableFactory;
	private final TrackObservableFactory trackObservableFactory;

	public GigWithTracksObservableFactory(
			GigObservableFactory gigObservableFactory,
			TrackObservableFactory trackObservableFactory) {
		this.gigObservableFactory = gigObservableFactory;
		this.trackObservableFactory = trackObservableFactory;
	}

	public Observable<GigWithTracks> create() {
		return gigObservableFactory
				.create()
				.flatMap(this::createGigWithTrackList);
	}

	private Observable<GigWithTracks> createGigWithTrackList(Gig gig) {
		Observable<GigWithTracks> gigWithoutTracks = Observable.just(new GigWithTracks(gig));
		Observable<GigWithTracks> gigWithTracks = trackObservableFactory
				.create(gig.getArtist())
				.toList()
				.filter((tracks) -> !tracks.isEmpty())
				.map((tracks) -> new GigWithTracks(gig, tracks));
		return Observable.concat(gigWithoutTracks, gigWithTracks);
	}
}
