package radiohere;

import org.json.JSONObject;

import rx.Observable;
import rx.util.async.Async;

public class VenueObservableFactory {
	private final SongKick songKick;

	public VenueObservableFactory(SongKick songKick) {
		this.songKick = songKick;
	}

	public Observable<Venue> create(Integer id) {
		if (id == null) {
			return Observable.empty();
		}
		return Async.fromCallable(() -> songKick.getVenue(id))
				.filter(this::canCreateVenue)
				.map(this::songKickToVenue);
	}

	public Venue songKickToVenue(String songKickJson) {
		JSONObject venue = extractVenue(songKickJson);
		return createVenue(venue);
	}
	
	private boolean canCreateVenue(String songKickJson) {
		JSONObject venue = extractVenue(songKickJson);
		return venue.has("lat") && venue.has("lng") && !venue.isNull("lat") && !venue.isNull("lng");
	}
	
	private JSONObject extractVenue(String songKickJson) {
		return new JSONObject(songKickJson)
				.getJSONObject("resultsPage")
				.getJSONObject("results")
				.getJSONObject("venue");
	}

	private Venue createVenue(JSONObject venue) {
		String name = venue.getString("displayName");
		String postcode = venue.getString("zip");
		double latitude = venue.getDouble("lat");
		double longitude = venue.getDouble("lng");
		return new Venue(name, postcode, new Coordinate(latitude, longitude));
	}
}
