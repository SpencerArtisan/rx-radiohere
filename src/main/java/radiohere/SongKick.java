package radiohere;

import org.apache.http.client.fluent.Request;

public class SongKick {
	private static final String API_KEY = "hZ33FKGXTbn0VeVh";
	private static final String LONDON = "24426";
	private static final String GIG_URL = "http://api.songkick.com/api/3.0/metro_areas/%s/calendar.json?apikey=%s&page=%s";
	private static final String VENUE_URL = "http://api.songkick.com/api/3.0/venues/%s.json?apikey=%s";

	public String getGigs(int page) throws SongKickException {
		return callApi(String.format(GIG_URL, LONDON, API_KEY, page));
	}

	public String getVenue(int id) throws SongKickException {
		return callApi(String.format(VENUE_URL, id, API_KEY));
	}

	private String callApi(String url) throws SongKickException {
		try {
			return Request.Get(url).execute().returnContent().asString();
		} catch (Exception e) {
			throw new SongKickException(e);
		}
	}
}
