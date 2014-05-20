package radiohere;

import org.json.JSONObject;

import rx.Observable;
import rx.util.async.Async;

public class VenueObservableFactory {
	private final SongKick songKick;

	public VenueObservableFactory(SongKick songKick) {
		this.songKick = songKick;
	}

	public Observable<Venue> create(int id) {
		return Async.fromCallable(() -> songKick.getVenue(id))
				.map(this::songKickToVenue);
	}
	public Venue songKickToVenue(String songKickJson) {
		JSONObject venue = extractVenue(songKickJson);
		return createVenue(venue);
	}
	
	private JSONObject extractVenue(String songKickJson) {
		return new JSONObject(songKickJson)
				.getJSONObject("resultsPage")
				.getJSONObject("results")
				.getJSONObject("venue");
	}

	private Venue createVenue(JSONObject event) {
		String name = event.getString("displayName");
		String postcode = event.getString("zip");
		return new Venue(name, postcode);
	}
}
