package com.artisan.radiohere;

import org.json.JSONObject;

class JsonVenue {
	private String songKickJson;

	public JsonVenue(String songKickJson) {
		this.songKickJson = songKickJson;
	}

	public Venue extract() {
		JSONObject jsonVenue = new JSONObject(songKickJson)
				.getJSONObject("resultsPage").getJSONObject("results")
				.getJSONObject("venue");
		return createVenue(jsonVenue);
	}

	private Venue createVenue(JSONObject jsonVenue) {
		if (!jsonVenue.has("lat") || !jsonVenue.has("lng")
				|| jsonVenue.isNull("lat") || jsonVenue.isNull("lng")) {
			return null;
		}
		String name = jsonVenue.getString("displayName");
		String postcode = jsonVenue.getString("zip");
		double latitude = jsonVenue.getDouble("lat");
		double longitude = jsonVenue.getDouble("lng");
		return new Venue(name, postcode, new Coordinate(latitude, longitude));
	}
}