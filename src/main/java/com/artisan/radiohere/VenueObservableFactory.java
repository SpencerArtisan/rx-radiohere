package com.artisan.radiohere;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

public class VenueObservableFactory {
	private final SongKick songKick;

	public VenueObservableFactory(SongKick songKick) {
		this.songKick = songKick;
	}

	public Observable<Venue> create(Integer id) {
		if (id == null) {
			return Observable.empty();
		}
		return Async.fromCallable(() -> songKick.getVenue(id), Schedulers.io())
				.map(this::songKickToVenue)
				.filter((venue) -> venue != null);
	}

	public Venue songKickToVenue(String songKickJson) {
		return new JsonVenue(songKickJson).extract();
	}
}
