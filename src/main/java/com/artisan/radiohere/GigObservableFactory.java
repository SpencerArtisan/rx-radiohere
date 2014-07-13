package com.artisan.radiohere;

import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

public class GigObservableFactory {
    private Logger logger = Logger.getLogger(this.getClass().getName());
	private final SongKick songKick;
	private final int pages;
	private final VenueObservableFactory venueObservableFactory;
	private final double maximumDistanceFromCentralLondon;

	public GigObservableFactory(SongKick songKick, VenueObservableFactory venueObservableFactory, int pages, double maximumDistanceFromCentralLondon) {
		this.songKick = songKick;
		this.venueObservableFactory = venueObservableFactory;
		this.pages = pages;
		this.maximumDistanceFromCentralLondon = maximumDistanceFromCentralLondon;
	}

	public GigObservableFactory() {
		this(new SongKick(), new VenueObservableFactory(new SongKick()), 12, 5);
	}

	public Observable<Gig> create() {
		return Observable
				.range(0, pages)
				.flatMap(this::createSongKickPageObservable)
				.flatMap(this::createGigObservable)
				.distinct()
				.flatMap(this::createGigWithVenueObservable)
				.filter(this::isClose);
	}

	private boolean isClose(Gig gig) {
		boolean isClose = gig.getDistance() < maximumDistanceFromCentralLondon;
		logger.info(gig.getArtist() + " is close enough? " + isClose + " (" + gig.getDistance() + "km)");
		return isClose;
	}
	
	private Observable<String> createSongKickPageObservable(Integer page) {
		return Async.fromCallable(() -> songKick.getGigs(page), Schedulers.trampoline());
	}
	
	public Observable<Gig> createGigObservable(String songKickPage) {
		return Observable
				.from(extractEvents(songKickPage))
				.filter(this::canCreateGig)
				.map(this::createGig);
	}
	
	private Observable<Gig> createGigWithVenueObservable(Gig gig) {
		return venueObservableFactory
				.create(gig.getVenueId())
				.map(gig::addVenue);
	}
		
	private boolean canCreateGig(JSONObject event) {
		return event.getJSONArray("performance").length() > 0;
	}

	private List<JSONObject> extractEvents(String songKickJson) {
		JSONArray events = new JSONObject(songKickJson)
				.getJSONObject("resultsPage")
				.getJSONObject("results")
				.getJSONArray("event");
		return JSONUtil.convertToList(events);
	}

	private Gig createGig(JSONObject event) {
		JSONObject performance = event
				.getJSONArray("performance")
				.getJSONObject(0);
		String bandName = performance.getString("displayName");
		String date = event.getJSONObject("start").getString("date");
		JSONObject venue = event.getJSONObject("venue");
		JSONObject artist = performance.getJSONObject("artist");
		String venueName = venue.getString("displayName");
		Integer venueId = venue.isNull("id") ? null : venue.getInt("id");
		Integer artistId = artist.isNull("id") ? null : artist.getInt("id");

		return new Gig(bandName, artistId, date, venueName, venueId, null);
	}
}
