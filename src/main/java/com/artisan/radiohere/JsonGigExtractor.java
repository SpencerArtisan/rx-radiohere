package com.artisan.radiohere;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;

class JsonGigExtractor {
	public Observable<Gig> extract(String songKickJson) {
		JSONArray events = new JSONObject(songKickJson)
				.getJSONObject("resultsPage").getJSONObject("results")
				.getJSONArray("event");
		List<JSONObject> eventsJson = JSONUtil.convertToList(events);
		return Observable
				.from(eventsJson)
				.map(this::createGig);
	}

	private Gig createGig(JSONObject event) {
		String bandName = null;
		
		if (event.getJSONArray("performance").length() != 0) {
			JSONObject performance = event.getJSONArray("performance").getJSONObject(0);
			bandName = performance.getString("displayName");
		}
		
		String date = event.getJSONObject("start").getString("date");
		JSONObject venue = event.getJSONObject("venue");
		Coordinate venueLocation = getLocation(venue);
		String venueName = venue.getString("displayName");
		String songkickUrl = event.getString("uri");
		return new Gig(bandName, date, venueName, venueLocation, songkickUrl);
	}

	private Coordinate getLocation(JSONObject venue) {
		Double latitude = venue.isNull("lat") ? null : venue.getDouble("lat");
		Double longitude = venue.isNull("lng") ? null : venue.getDouble("lng");
		return latitude == null || longitude == null ? null : new Coordinate(latitude, longitude);
	}
}