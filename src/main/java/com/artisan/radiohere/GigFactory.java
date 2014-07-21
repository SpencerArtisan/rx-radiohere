package com.artisan.radiohere;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GigFactory {
	private static final String CACHE_KEY = "LONDON";
	private static final int DEFAULT_SONGKICK_PAGES = 18;
	private final LoadingCache<String, Observable<Gig>> cachedGigs;
	private final TrackFactory trackFactory;

	public GigFactory() {
		this(new TrackFactory(), new SongKick(), new JsonGigExtractor(), DEFAULT_SONGKICK_PAGES);
	}

	public GigFactory(TrackFactory trackFactory, SongKick songKick, JsonGigExtractor jsonGigExtractor, int pages) {
		this.trackFactory = trackFactory;
		this.cachedGigs = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(
				new CacheLoader<String, Observable<Gig>>() {
					@Override
					public Observable<Gig> load(String area) throws Exception {
						return Observable.range(0, pages)
								.flatMap(this::pageNumberToJson)
								.flatMap(jsonGigExtractor::extract).distinct()
								.filter(Gig::hasArtist)
								.filter(Gig::hasVenue);
					}
			
					private Observable<String> pageNumberToJson(Integer page) {
						return Async.fromCallable(() -> songKick.getGigs(page), Schedulers.trampoline());
					}
				});
	}

	public Observable<Gig> create(Coordinate origin, double maximumDistanceFromOrigin) {
		try {
			return cachedGigs.get(CACHE_KEY)
							.map((gig) -> gig.setDistance(origin.kmFrom(gig.getVenue())))
							.filter((gig) -> gig.isDistanceWithinKm(maximumDistanceFromOrigin))
							.flatMap(this::addTracks);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private Observable<Gig> addTracks(Gig gig) {
		Observable<Track> trackObservable = trackFactory.create(gig.getArtist());
		return trackObservable.reduce(gig, Gig::addTrack);
	}
}
