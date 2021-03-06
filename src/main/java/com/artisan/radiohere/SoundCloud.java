package com.artisan.radiohere;

import java.net.URLEncoder;
import java.util.logging.Logger;

public class SoundCloud {
	private static final String CLIENT_ID = "ab2cd50270f2b1097c169d43f06a3d17";
	private static final String TRACK_URL = "http://api.soundcloud.com/tracks.json?q=%s&client_id=%s";
	private static final int RETRIES = 100;
	private static final int MS_BETWEEN_RETRIES = 1000;

	public String getClientId() {
		return CLIENT_ID;
	}
	
	public String getTracks(String artist) throws ApiException {
		String url = String.format(TRACK_URL, URLEncoder.encode(artist), CLIENT_ID);
		return new ApiRetrier(RETRIES, MS_BETWEEN_RETRIES, url).execute();
	}
}
