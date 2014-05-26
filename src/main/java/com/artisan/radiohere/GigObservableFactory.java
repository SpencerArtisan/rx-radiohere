package com.artisan.radiohere;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.util.async.Async;

public class GigObservableFactory {
	private final Coordinate OLD_STREET = new Coordinate(51.5265, -0.0825);
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
		this(new SongKick(), new VenueObservableFactory(new SongKick()), 4, 3);
	}

	public Observable<Gig> create() {
		return Observable
				.range(0, pages)
				.flatMap(this::createSongKickPageObservable)
				.flatMap(this::createGigObservable)
				.flatMap(this::createGigWithVenueObservable)
				.filter((gig) -> gig.getVenue().getCoordinate().kmFrom(OLD_STREET) < maximumDistanceFromCentralLondon);
	}

	private Observable<String> createSongKickPageObservable(Integer page) {
		return Async.fromCallable(() -> songKick.getGigs(page));
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
		String bandName = event
				.getJSONArray("performance")
				.getJSONObject(0)
				.getString("displayName");
		String date = event
				.getJSONObject("start")
				.getString("date");
		JSONObject venue = event.getJSONObject("venue");
		String venueName = venue.getString("displayName");
		Integer venueId = venue.isNull("id") ? null : venue.getInt("id");

		return new Gig(bandName, date, venueName, venueId, null);
	}
}