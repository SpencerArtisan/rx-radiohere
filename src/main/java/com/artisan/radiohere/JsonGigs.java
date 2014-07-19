package com.artisan.radiohere;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;

class JsonGigs {
	public Observable<Gig> extract(String songKickJson) {
		JSONArray events = new JSONObject(songKickJson)
				.getJSONObject("resultsPage").getJSONObject("results")
				.getJSONArray("event");
		List<JSONObject> eventsJson = JSONUtil.convertToList(events);
		return Observable
				.from(eventsJson)
				.map(this::createGig)
				.filter((gig) -> gig != null);
	}

	private Gig createGig(JSONObject event) {
		String bandName = null;
		Integer artistId = null;
		
		if (event.getJSONArray("performance").length() != 0) {
			JSONObject performance = event.getJSONArray("performance")
					.getJSONObject(0);
			bandName = performance.getString("displayName");
			JSONObject artist = performance.getJSONObject("artist");
			artistId = artist.isNull("id") ? null : artist.getInt("id");
		}
		
		String date = event.getJSONObject("start").getString("date");
		JSONObject venue = event.getJSONObject("venue");
		String venueName = venue.getString("displayName");
		Integer venueId = venue.isNull("id") ? null : venue.getInt("id");
		Coordinate venueLocation = getLocation(venue);
		return new Gig(bandName, artistId, date, venueName, venueId, venueLocation);
	}

	private Coordinate getLocation(JSONObject venue) {
		Double latitude = venue.isNull("lat") ? null : venue.getDouble("lat");
		Double longitude = venue.isNull("lng") ? null : venue.getDouble("lng");
		return latitude == null || longitude == null ? null : new Coordinate(latitude, longitude);
	}
}