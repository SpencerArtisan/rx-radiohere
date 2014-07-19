package com.artisan.radiohere;

import java.util.List;
import java.util.logging.Logger;

import rx.Observable;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

public class GigFactory {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private final SongKick songKick;
	private final int pages;
	private final double maximumDistanceFromCentralLondon;
	private final TrackObservableFactory trackObservableFactory;

	public GigFactory(TrackObservableFactory trackObservableFactory, SongKick songKick, int pages, double maximumDistanceFromCentralLondon) {
		this.songKick = songKick;
		this.pages = pages;
		this.maximumDistanceFromCentralLondon = maximumDistanceFromCentralLondon;
		this.trackObservableFactory = trackObservableFactory;
	}

	public GigFactory() {
		this(new TrackObservableFactory(), new SongKick(), 12, 5);
	}

	public Observable<Gig> create() {
		return Observable.range(0, pages)
				.flatMap(this::toSongkickPage)
				.flatMap(this::songKickToGigs).distinct()
				.filter(Gig::hasArtist)
				.filter(Gig::hasVenue)
				.filter(this::isClose)
				.flatMap(this::addTracks);
	}

	public Observable<Gig> addTracks(Gig gig) {
		Observable<Track> trackObservable = trackObservableFactory.create(gig.getArtist());
		return trackObservable.reduce(gig, Gig::addTrack);
	}

	private boolean isClose(Gig gig) {
		return gig.isVenueWithinKm(Coordinate.YEATE_STREET, maximumDistanceFromCentralLondon);
	}
	
	private Observable<String> toSongkickPage(Integer page) {
		return Async.fromCallable(() -> songKick.getGigs(page),
				Schedulers.trampoline());
	}

	public Observable<Gig> songKickToGigs(String songKickJson) {
		return new JsonGigs(songKickJson).extract();
	}
}
