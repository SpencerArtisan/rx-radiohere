package com.artisan.radiohere;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;

class JsonTrackExtractor {
	public Observable<Track> extract(String soundCloudJson, String soundCloudClientId) {
		List<JSONObject> tracksJson = JSONUtil.convertToList(new JSONArray(soundCloudJson));
		return Observable
				.from(tracksJson)
				.map((json) -> createTrack(json, soundCloudClientId));
	}

	private Track createTrack(JSONObject track, String soundCloudClientId) {
		String name = track.getString("title");
		String streamUrl = track.has("stream_url") 
				? track.getString("stream_url") + "?client_id=" + soundCloudClientId : null;
		return new Track(name, streamUrl);
	}
}