package com.artisan.radiohere;

import java.util.List;

public class ArtistGenre {
	private final List<String> genres;
	private final String name;
	
	public ArtistGenre(String name, List<String> genres) {
		this.name = name;
		this.genres = genres;
	}

	public List<String> getGenres() {
		return genres;
	}

	public String getArtist() {
		return name;
	}

	@Override
	public String toString() {
		return "ArtistGenre [name=" + name + ", genres=" + genres + "]";
	}
}
