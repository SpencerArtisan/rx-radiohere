package com.artisan.radiohere;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;

class JsonGigs {
	private String songKickJson;

	public JsonGigs(String songKickJson) {
		this.songKickJson = songKickJson;
	}

	public Observable<Gig> extract() {
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
		if (event.getJSONArray("performance").length() == 0) {
			return null;
		}
		JSONObject performance = event.getJSONArray("performance")
				.getJSONObject(0);
		return performanceToGig(event, performance);
	}

	private Gig performanceToGig(JSONObject event, JSONObject performance) {
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