package com.artisan.radiohere;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TrackFactory {
	private final LoadingCache<String, Observable<Track>> cachedTracks;

	public TrackFactory(SoundCloud soundCloud, int maxTracks) {
		this.cachedTracks = CacheBuilder.newBuilder().expireAfterAccess(4, TimeUnit.DAYS).build(
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

	public TrackFactory() {
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
