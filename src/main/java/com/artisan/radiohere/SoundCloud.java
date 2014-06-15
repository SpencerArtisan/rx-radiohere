package com.artisan.radiohere;

import java.net.URLEncoder;
import java.util.logging.Logger;

import org.apache.http.client.fluent.Request;

public class SoundCloud {
    private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final String CLIENT_ID = "ab2cd50270f2b1097c169d43f06a3d17";
	private static final String TRACK_URL = "http://api.soundcloud.com/tracks.json?q=%s&client_id=%s";
	private static final int RETRIES = 10;
	private static final int MS_BETWEEN_RETRIES = 10000;

	public String getClientId() {
		return CLIENT_ID;
	}
	
	public String getTracks(String artist) throws ApiException {
		logger.info("Call SoundCloud for " + artist); 
		String url = String.format(TRACK_URL, URLEncoder.encode(artist), CLIENT_ID);
		return new ApiRetrier(RETRIES, MS_BETWEEN_RETRIES, url).execute();
	}
}
