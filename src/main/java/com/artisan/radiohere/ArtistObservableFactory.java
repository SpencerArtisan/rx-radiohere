package com.artisan.radiohere;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observables.GroupedObservable;

public class ArtistObservableFactory {
	private final GigObservableFactory gigObservableFactory;
	private final TrackObservableFactory trackObservableFactory;
	private ArtistGenreObservableFactory artistGenreObservableFactory;

	public ArtistObservableFactory(GigObservableFactory gigObservableFactory,
			TrackObservableFactory trackObservableFactory,
			ArtistGenreObservableFactory artistGenreObservableFactory) {
		this.gigObservableFactory = gigObservableFactory;
		this.trackObservableFactory = trackObservableFactory;
		this.artistGenreObservableFactory = artistGenreObservableFactory;
	}

	public ArtistObservableFactory() {
		this(new GigObservableFactory(), new TrackObservableFactory(), new ArtistGenreObservableFactory());
	}

	public Observable<Artist> create() {
		return gigObservableFactory
				.create()
				.groupBy(Gig::getArtist)
				.flatMap(this::createArtist)
				.flatMap(this::createArtistWithGenreObserable);
	}
	
	public Observable<Artist> createArtist(GroupedObservable<String, Gig> gigObservableForArtist) {
		return Observable.zip(
				createCumulativeGigsForArtistObserver(gigObservableForArtist), 
				createTrackListSingletonObserver(gigObservableForArtist).repeat(5), 
				Artist::new);
	}

	private Observable<ArrayList<Gig>> createCumulativeGigsForArtistObserver(
			GroupedObservable<String, Gig> gigObservableForArtist) {
		return gigObservableForArtist
				.asObservable()
				.scan(new ArrayList<>(), this::append)
				.filter((gigs) -> !gigs.isEmpty());
	}

	private Observable<List<Track>> createTrackListSingletonObserver(
			GroupedObservable<String, Gig> gigObservableForArtist) {
		return trackObservableFactory
			.create(gigObservableForArtist.getKey())
			.toList();
	}
	
	private Observable<Artist> createArtistWithGenreObserable(Artist artist) {
		return artistGenreObservableFactory
				.create(artist.getGigs().get(0).getSongkickArtistId())
				.map(artist::addGenre);
	}
		
	public ArrayList<Gig> append(ArrayList<Gig> gigs, Gig gig) {
		ArrayList<Gig> newList = new ArrayList<>(gigs);
		newList.add(gig);
		return newList;
	}
}
