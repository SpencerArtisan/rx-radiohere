package com.artisan.radiohere;

import java.net.URLEncoder;

import org.apache.http.client.fluent.Request;

public class SoundCloud {
	private static final String CLIENT_ID = "ab2cd50270f2b1097c169d43f06a3d17";
	private static final String TRACK_URL = "http://api.soundcloud.com/tracks.json?q=%s&client_id=%s";

	public String getClientId() {
		return CLIENT_ID;
	}
	
	public String getTracks(String artist) throws SoundCloudException {
		return callApi(String.format(TRACK_URL, URLEncoder.encode(artist), CLIENT_ID));
	}

	private String callApi(String url) throws SoundCloudException {
		try {
			return Request.Get(url).execute().returnContent().asString();
		} catch (Exception e) {
			throw new SoundCloudException(e);
		}
	}
}
