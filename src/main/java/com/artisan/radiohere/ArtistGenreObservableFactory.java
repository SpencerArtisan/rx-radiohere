package com.artisan.radiohere;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

public class ArtistGenreObservableFactory {
    private Logger logger = Logger.getLogger(this.getClass().getName());
	private EchoNest echoNest;

	public ArtistGenreObservableFactory() {
		this(new EchoNest());
	}

	public ArtistGenreObservableFactory(EchoNest echoNest) {
		this.echoNest = echoNest;
	}

	public Observable<ArtistGenre> create(Integer songkickArtistId) {
		return Async.fromCallable(() -> echoNest.getArtist(songkickArtistId), Schedulers.io())
				.filter(this::canCreateArtist)
				.map(this::echoNestToArtist)
				.filter(this::isInteresting);
	}

	public ArtistGenre echoNestToArtist(String echoNestJson) {
		JSONObject artist = extractArtist(echoNestJson);
		return createArtistGenre(artist);
	}
	
	private boolean canCreateArtist(String echoNestJson) {
		return new JSONObject(echoNestJson).getJSONObject("response").has("artist");
	}
	
	private JSONObject extractArtist(String echoNestJson) {
		return new JSONObject(echoNestJson)
				.getJSONObject("response")
				.getJSONObject("artist");
	}

	private ArtistGenre createArtistGenre(JSONObject artist) {
		String artistName = artist.getString("name");
		JSONArray artistGenres = artist.getJSONArray("genres");
		Stream<JSONObject> genres = JSONUtil.convertToList(artistGenres).stream();
		List<String> genreNames = genres
				.map((genreJson) -> genreJson.getString("name"))
				.collect(Collectors.toList());
		return new ArtistGenre(artistName, genreNames);
	}
	
	private boolean isInteresting(ArtistGenre genre) {
		List<String> genres = genre.getGenres();
		boolean interesting = isInteresting(genres);
		logger.info(genre.toString() + " is interesting? " + interesting);
		return interesting;
	}

	private boolean isInteresting(List<String> genres) {
		boolean anyInteresting = genres.stream().anyMatch((name) -> 
			name.contains("psychedel") ||
		  	name.contains("folk") ||
		  	name.contains("iceland") ||
		  	name.contains("nordic") ||
		  	name.contains("norwegian") ||
		  	name.contains("stomp") ||
		  	name.contains("lo-fi") ||
		  	name.contains("danish") ||
		  	name.contains("swedish") ||
		  	name.contains("french") ||
		  	name.contains("indie"));
		return anyInteresting;
	}
}
