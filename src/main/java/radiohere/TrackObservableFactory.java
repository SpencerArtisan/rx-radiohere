package radiohere;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.util.async.Async;

public class TrackObservableFactory {
	private SoundCloud soundCloud;

	public TrackObservableFactory(SoundCloud soundCloud) {
		this.soundCloud = soundCloud;
	}

	public Observable<Track> create(String artist) {
		Observable<String> soundCloudObservable = Async.fromCallable(() -> {
			return soundCloud.getTracks(artist);
		});
		
		return soundCloudObservable.flatMap(this::soundCloudToTracks);
	}
	
	public Observable<Track> soundCloudToTracks(String soundCloudJson) {
		return Observable.create((Subscriber<? super Track> subscriber) -> {
			JSONArray events = extractTracks(soundCloudJson);
			for (int i = 0; i < events.length(); i++) {
				subscriber.onNext(createTrack((JSONObject) events.get(i)));
			}
			subscriber.onCompleted();
		});
	}
	
	private JSONArray extractTracks(String soundCloudJson) {
		return new JSONArray(soundCloudJson);
	}

	private Track createTrack(JSONObject track) {
		String name = track.getString("title");
		String streamUrl = track.getString("stream_url") + "?client_id=" + soundCloud.getClientId();

		return new Track(name, streamUrl);
	}

}
