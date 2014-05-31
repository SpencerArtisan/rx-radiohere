package com.artisan.radiohere;


public class SongKick {
	private static final String API_KEY = "hZ33FKGXTbn0VeVh";
	private static final String LONDON = "24426";
	private static final String GIG_URL = "http://api.songkick.com/api/3.0/metro_areas/%s/calendar.json?apikey=%s&page=%s";
	private static final String VENUE_URL = "http://api.songkick.com/api/3.0/venues/%s.json?apikey=%s";
	private static final int RETRIES = 10;
	private static final int MS_BETWEEN_RETRIES = 10000;

	public String getGigs(int page) throws ApiException {
		String url = String.format(GIG_URL, LONDON, API_KEY, page);
		return new ApiRetrier(RETRIES, MS_BETWEEN_RETRIES, url).execute();
	}
	
	public String getVenue(int id) throws ApiException {
		String url = String.format(VENUE_URL, id, API_KEY);
		return new ApiRetrier(RETRIES, MS_BETWEEN_RETRIES, url).execute();
	}
}
