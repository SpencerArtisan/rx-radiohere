package com.artisan.radiohere;

import java.util.List;

public class ArtistGenre {
	private final List<String> genres;
	
	public ArtistGenre(List<String> genres) {
		this.genres = genres;
	}

	public List<String> getGenres() {
		return genres;
	}

	@Override
	public String toString() {
		return "ArtistGenre [genres=" + genres + "]";
	}
}
