package com.artisan.radiohere;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

public class TrackObservableFactory {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private SoundCloud soundCloud;
	private int maxTracks;
	private LoadingCache<String, Observable<Track>> cachedTracks;

	public TrackObservableFactory(SoundCloud soundCloud, int maxTracks) {
		this.soundCloud = soundCloud;
		this.maxTracks = maxTracks;
		this.cachedTracks = CacheBuilder.newBuilder().build(
				new CacheLoader<String, Observable<Track>>() {
					@Override
					public Observable<Track> load(String artist)
							throws Exception {
						return Async.fromCallable(() -> soundCloud.getTracks(artist), Schedulers.io())
								.flatMap((page) -> Observable
												.from(extractTracks(page))
												.filter(this::canCreateTrack)
												.take(maxTracks)
												.map(this::createTrack))
												.replay()
												.refCount();
					}

					private List<JSONObject> extractTracks(String soundCloudJson) {
						return JSONUtil.convertToList(new JSONArray(soundCloudJson));
					}

					private boolean canCreateTrack(JSONObject track) {
						return track.has("stream_url");
					}

					private Track createTrack(JSONObject track) {
						String name = track.getString("title");
						String streamUrl = track.getString("stream_url")
								+ "?client_id=" + soundCloud.getClientId();
						return new Track(name, streamUrl);
					}
				});
	}

	public TrackObservableFactory() {
		this(new SoundCloud(), 4);
	}

	 public Observable<Track> create(String artist) {
		 try {
			return cachedTracks.get(artist);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	 }
}
