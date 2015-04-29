package com.artisan.radiohere;

import java.lang.Exception;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;

class JsonGigExtractor {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public Observable<Gig> extract(String songKickJson) {
        try {
            JSONArray events = new JSONObject(songKickJson)
                    .getJSONObject("resultsPage").getJSONObject("results")
                    .getJSONArray("event");
            List<JSONObject> eventsJson = JSONUtil.convertToList(events);
            return Observable
                    .from(eventsJson)
                    .map(this::createGig);
        } catch (Exception e) {
            logger.warning("Failed to extract gig from songkick json: " + songKickJson + "  Error: " + e.getMessage());
            return Observable.empty();
        }
    }

	private Gig createGig(JSONObject event) {
		String bandName = null;
		
		if (event.getJSONArray("performance").length() != 0) {
            try {
                JSONObject performance = event.getJSONArray("performance").getJSONObject(0);
                bandName = performance.getString("displayName");
            } catch (Exception e) {
                logger.warning("Failed to extract bandname json: " + event + "  Error: " + e.getMessage());
                bandName = "Unknown";
            }
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