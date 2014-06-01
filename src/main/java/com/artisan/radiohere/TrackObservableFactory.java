package com.artisan.radiohere;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

public class TrackObservableFactory {
	private SoundCloud soundCloud;
	private int maxTracks;

	public TrackObservableFactory(SoundCloud soundCloud, int maxTracks) {
		this.soundCloud = soundCloud;
		this.maxTracks = maxTracks;
	}

	public TrackObservableFactory() {
		this(new SoundCloud(), 4);
	}

	public Observable<Track> create(String artist) {
		return createSoundCloudPageObservable(artist)
				.flatMap(this::createTrackObservable);
	}

	private Observable<String> createSoundCloudPageObservable(String artist) {
		return Async.fromCallable(() -> soundCloud.getTracks(artist), Schedulers.io());
	}
	
	public Observable<Track> createTrackObservable(String soundCloudJson) {
		return Observable
				.from(extractTracks(soundCloudJson))
				.filter(this::canCreateTrack)
				.take(maxTracks)
				.map(this::createTrack);
	}
	
	private List<JSONObject> extractTracks(String soundCloudJson) {
		return JSONUtil.convertToList(new JSONArray(soundCloudJson));
	}

	private boolean canCreateTrack(JSONObject track) {
		return track.has("stream_url");
	}

	private Track createTrack(JSONObject track) {
		String name = track.getString("title");
		String streamUrl = track.getString("stream_url") + "?client_id=" + soundCloud.getClientId();
		return new Track(name, streamUrl);
	}
}
