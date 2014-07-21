package com.artisan.radiohere;

import java.util.concurrent.*;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

import com.google.common.cache.*;

public class TrackFactory {
	private static final int DEFAULT_MAX_TRACKS = 4;
	private final LoadingCache<String, Observable<Track>> cachedTracks;

	public TrackFactory() {
		this(new SoundCloud(), new JsonTrackExtractor(), DEFAULT_MAX_TRACKS);
	}

	public TrackFactory(SoundCloud soundCloud, JsonTrackExtractor jsonTrackExtractor, int maxTracks) {
		this.cachedTracks = CacheBuilder.newBuilder().expireAfterAccess(4, TimeUnit.DAYS).build(
				new CacheLoader<String, Observable<Track>>() {
					@Override
					public Observable<Track> load(String artist) throws Exception {
						return artistToJson(artist)
								.flatMap((json) -> jsonTrackExtractor.extract(json, soundCloud.getClientId()))
								.filter(Track::hasStreamUrl)
								.take(maxTracks)
								.replay()
								.refCount();
					}
			
					private Observable<String> artistToJson(String artist) {
						return Async.fromCallable(() -> soundCloud.getTracks(artist), Schedulers.io());
					}
				});
	}

	public Observable<Track> create(String artist) {
		try {
			return cachedTracks.get(artist);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
