package com.artisan.radiohere;

import java.util.logging.Logger;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

public class GigObservableFactory {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private final SongKick songKick;
	private final int pages;
	private final VenueObservableFactory venueObservableFactory;
	private final double maximumDistanceFromCentralLondon;

	public GigObservableFactory(SongKick songKick,
			VenueObservableFactory venueObservableFactory, int pages,
			double maximumDistanceFromCentralLondon) {
		this.songKick = songKick;
		this.venueObservableFactory = venueObservableFactory;
		this.pages = pages;
		this.maximumDistanceFromCentralLondon = maximumDistanceFromCentralLondon;
	}

	public GigObservableFactory() {
		this(new SongKick(), new VenueObservableFactory(new SongKick()), 12, 5);
	}

	public Observable<Gig> create() {
		return Observable.range(0, pages)
				.flatMap(this::toSongkickPage)
				.flatMap(this::songKickToGigs).distinct()
				.flatMap(this::createGigWithVenueObservable)
				.filter(this::isClose);
	}

	private boolean isClose(Gig gig) {
		boolean isClose = gig.getDistance() < maximumDistanceFromCentralLondon;
		logger.info(gig.getArtist() + " is close enough? " + isClose + " ("
				+ gig.getDistance() + "km)");
		return isClose;
	}

	private Observable<String> toSongkickPage(Integer page) {
		return Async.fromCallable(() -> songKick.getGigs(page),
				Schedulers.trampoline());
	}

	public Observable<Gig> songKickToGigs(String songKickJson) {
		return new JsonGigs(songKickJson).extract();
	}

	private Observable<Gig> createGigWithVenueObservable(Gig gig) {
		return venueObservableFactory.create(gig.getVenueId()).map(
				gig::addVenue);
	}
	
}
