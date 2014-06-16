package com.artisan.radiohere;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import rx.Observable;
import rx.observables.GroupedObservable;

public class ArtistObservableFactory {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final GigObservableFactory gigObservableFactory;
	private final TrackObservableFactory trackObservableFactory;

	public ArtistObservableFactory(GigObservableFactory gigObservableFactory,
			TrackObservableFactory trackObservableFactory) {
		this.gigObservableFactory = gigObservableFactory;
		this.trackObservableFactory = trackObservableFactory;
	}

	public ArtistObservableFactory() {
		this(new GigObservableFactory(), new TrackObservableFactory());
	}

	public Observable<Artist> create() {
		return gigObservableFactory
				.create()
				.groupBy(Gig::getArtistId)
				.flatMap(this::createArtist);
	}
	
	public Observable<Artist> createArtist(GroupedObservable<ArtistId, Gig> gigObservableForArtist) {
		return Observable.combineLatest(
				createCumulativeGigsForArtistObserver(gigObservableForArtist), 
				createTrackListSingletonObservable(gigObservableForArtist), 
				Artist::new);		
	}

	private Observable<ArrayList<Gig>> createCumulativeGigsForArtistObserver(
			GroupedObservable<ArtistId, Gig> gigObservableForArtist) {
		return gigObservableForArtist
				.asObservable()
				.scan(new ArrayList<>(), this::append)
				.filter((gigs) -> !gigs.isEmpty());
	}

	private Observable<List<Track>> createTrackListSingletonObservable(
			GroupedObservable<ArtistId, Gig> gigObservableForArtist) {
		return trackObservableFactory
			.create(gigObservableForArtist.getKey().getName())
			.toList();
	}
	
	public ArrayList<Gig> append(ArrayList<Gig> gigs, Gig gig) {
		ArrayList<Gig> newList = new ArrayList<>(gigs);
		newList.add(gig);
		return newList;
	}
}
