package com.artisan.radiohere;

public class EchoNest {
	private static final String API_KEY = "VPJWD4X2Y2FPFSUKE";
	private static final String ARTIST_URL = "http://developer.echonest.com/api/v4/artist/profile?api_key=%s&id=songkick:artist:%s&bucket=genre&format=json";
	private static final int RETRIES = 10;
	private static final int MS_BETWEEN_RETRIES = 60000;

	public String getArtist(int id) throws ApiException {
		String url = String.format(ARTIST_URL, API_KEY, id);
		return new ApiRetrier(RETRIES, MS_BETWEEN_RETRIES, url).execute();
	}
}
