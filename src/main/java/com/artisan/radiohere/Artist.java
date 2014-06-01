package com.artisan.radiohere;

import java.util.List;
import java.util.logging.Logger;

public class Artist {
    private Logger logger = Logger.getLogger(this.getClass().getName());
	private final List<Gig> gigs;
	private final List<Track> tracks;
	private final ArtistGenre genre;

	public Artist(List<Gig> gigs, List<Track> tracks) {
		this(gigs, tracks, null);
		logger.info("Created " + toString());
	}
	
	public Artist(List<Gig> gigs, List<Track> tracks, ArtistGenre genre) {
		this.gigs = gigs;
		this.tracks = tracks;
		this.genre = genre;
	}

	public List<Gig> getGigs() {
		return gigs;
	}

	public List<Track> getTracks() {
		return tracks;
	}
	
	public String getName() {
		return gigs.get(0).getArtist();
	}
	
	public ArtistGenre getGenre() {
		return genre;
	}
	
	public Artist addGenre(ArtistGenre artistGenre) {
		logger.info("Adding genres to " + getName() + ": " + artistGenre.getGenres());
		return new Artist(gigs, tracks, artistGenre);
	}

	@Override
	public String toString() {
		return "Artist [name=" + getName() + ", genre=" + genre
				+ ", gigs=" + gigs + ", tracks=" + tracks + "]";
	}
	
}
