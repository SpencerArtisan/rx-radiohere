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
		Observable<Gig> gigObservable = gigObservableFactory.create();
		return gigObservable.flatMap((gig) -> createGigWithGrowingTrackList(gig));
	}

	private Observable<GigWithTracks> createGigWithGrowingTrackList(Gig gig) {
		Observable<Track> create = trackObservableFactory.create(gig.getArtist());
		return create.scan(new GigWithTracks(gig), 
				(existingGigWithTracks, newTrack) -> existingGigWithTracks.add(newTrack));
	}
}
