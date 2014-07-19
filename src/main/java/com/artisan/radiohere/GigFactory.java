package com.artisan.radiohere;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GigFactory {
	private final LoadingCache<String, Observable<Gig>> cachedGigs;

	public GigFactory(TrackFactory trackFactory, SongKick songKick, JsonGigs jsonGigs, int pages, double maximumDistanceFromCentralLondon) {
		this.cachedGigs = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(
				new CacheLoader<String, Observable<Gig>>() {
					@Override
					public Observable<Gig> load(String area)
							throws Exception {
						return Observable.range(0, pages)
								.flatMap(this::toSongkickPage)
								.flatMap(this::songKickToGigs).distinct()
								.filter(Gig::hasArtist)
								.filter(Gig::hasVenue)
								.filter(this::isClose)
								.flatMap(this::addTracks);
					}
			
					public Observable<Gig> addTracks(Gig gig) {
						Observable<Track> trackObservable = trackFactory.create(gig.getArtist());
						return trackObservable.reduce(gig, Gig::addTrack);
					}
			
					private boolean isClose(Gig gig) {
						return gig.isVenueWithinKm(Coordinate.YEATE_STREET, maximumDistanceFromCentralLondon);
					}
					
					private Observable<String> toSongkickPage(Integer page) {
						return Async.fromCallable(() -> songKick.getGigs(page),
								Schedulers.trampoline());
					}
			
					public Observable<Gig> songKickToGigs(String songKickJson) {
						return jsonGigs.extract(songKickJson);
					}
				});
	}

	public GigFactory() {
		this(new TrackFactory(), new SongKick(), new JsonGigs(), 28, 5);
	}

	public Observable<Gig> create() {
		try {
			return cachedGigs.get("LONDON");
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
