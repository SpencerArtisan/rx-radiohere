package com.artisan.radiohere;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.util.async.Async;

public class ArtistGenreObservableFactory {
	private EchoNest echoNest;

	public ArtistGenreObservableFactory() {
		this(new EchoNest());
	}

	public ArtistGenreObservableFactory(EchoNest echoNest) {
		this.echoNest = echoNest;
	}

	public Observable<ArtistGenre> create(Integer songkickArtistId) {
		return Async.fromCallable(() -> echoNest.getArtist(songkickArtistId))
				.filter(this::canCreateArtist)
				.map(this::echoNestToArtist);
	}

	public ArtistGenre echoNestToArtist(String echoNestJson) {
		JSONArray artistGenres = extractArtistGenres(echoNestJson);
		return createArtistGenre(artistGenres);
	}
	
	private boolean canCreateArtist(String echoNestJson) {
		JSONArray artistGenres = extractArtistGenres(echoNestJson);
		return true;
	}
	
	private JSONArray extractArtistGenres(String echoNestJson) {
		return new JSONObject(echoNestJson)
				.getJSONObject("response")
				.getJSONObject("artist")
				.getJSONArray("artistGenres");
	}

	private ArtistGenre createArtistGenre(JSONArray artistGenres) {
		Stream<JSONObject> genres = JSONUtil.convertToList(artistGenres).stream();
		List<String> genreNames = genres
				.map((genreJson) -> genreJson.getString("name"))
				.collect(Collectors.toList());
		return new ArtistGenre(genreNames);
	}
}
