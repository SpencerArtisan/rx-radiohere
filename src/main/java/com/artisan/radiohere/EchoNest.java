package com.artisan.radiohere;

import org.apache.http.client.fluent.Request;

public class EchoNest {
	private static final String API_KEY = "VPJWD4X2Y2FPFSUKE";
	private static final String ARTIST_URL = "http://developer.echonest.com/api/v4/artist/profile?api_key=%s&id=songkick:artist:%s&bucket=genre&format=json";

	public String getArtist(int id) throws EchoNestException {
		return callApi(String.format(ARTIST_URL, API_KEY, id));
	}

	private String callApi(String url) throws EchoNestException {
		try {
			return Request.Get(url).execute().returnContent().asString();
		} catch (Exception e) {
			throw new EchoNestException(e);
		}
	}
}
