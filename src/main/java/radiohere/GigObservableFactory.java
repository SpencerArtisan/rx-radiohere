package radiohere;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.util.async.Async;

public class GigObservableFactory {
	private final SongKick songKick;
	private final int pages;
	private VenueObservableFactory venueObservableFactory;

	public GigObservableFactory(SongKick songKick, VenueObservableFactory venueObservableFactory, int pages) {
		this.songKick = songKick;
		this.venueObservableFactory = venueObservableFactory;
		this.pages = pages;
	}

	public Observable<Gig> create() {
		Observable<Integer> pagesObservable = Observable.range(0, pages);
		Observable<String> songKickResultPages = pagesObservable.flatMap((page) -> createSinglePageGigObservable(page));
		Observable<Gig> gigs = songKickResultPages.flatMap(this::songKickToGigs);
		return gigs.flatMap(this::gigToGigWithVenue);
	}

	private Observable<Gig> gigToGigWithVenue(Gig gig) {
		if (gig.getVenueId() == null) {
			return Observable.empty();
		}
		return venueObservableFactory.create(gig.getVenueId()).map((venue) -> gig.addVenue(venue));
	}
	
	private Observable<String> createSinglePageGigObservable(Integer page) {
		return Async.fromCallable(() -> songKick.getGigs(page));
	}
	
	public Observable<Gig> songKickToGigs(String songKickJson) {
		return Observable.create((Subscriber<? super Gig> subscriber) -> {
			JSONArray events = extractEvents(songKickJson);
			for (int i = 0; i < events.length(); i++) {
				if (canCreateGig(events.getJSONObject(i))) {
					subscriber.onNext(createGig(events.getJSONObject(i)));
				}
			}
			subscriber.onCompleted();
		});
	}
	
	private boolean canCreateGig(JSONObject event) {
		return event.getJSONArray("performance").length() > 0;
	}

	private JSONArray extractEvents(String songKickJson) {
		return new JSONObject(songKickJson)
				.getJSONObject("resultsPage")
				.getJSONObject("results")
				.getJSONArray("event");
	}

	private Gig createGig(JSONObject event) {
		String bandName = event
				.getJSONArray("performance")
				.getJSONObject(0)
				.getString("displayName");
		String date = event
				.getJSONObject("start")
				.getString("date");
		JSONObject venue = event.getJSONObject("venue");
		String venueName = venue.getString("displayName");
		Integer venueId = venue.isNull("id") ? null : venue.getInt("id");

		return new Gig(bandName, date, venueName, venueId, null);
	}
}
