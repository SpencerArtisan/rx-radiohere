package com.artisan.radiohere;

import java.util.List;
import java.util.logging.Logger;

public class Artist {
    private Logger logger = Logger.getLogger(this.getClass().getName());
	private final List<Gig> gigs;
	private final List<Track> tracks;
	private final ArtistGenre genre;

	public Artist(List<Gig> gigs, List<Track> tracks, ArtistGenre genre) {
		this.gigs = gigs;
		this.tracks = tracks;
		this.genre = genre;
		logger.info("Created Artist " + this);
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
	
	@Override
	public String toString() {
		return "Artist [name=" + getName() + ", genre=" + genre
				+ ", gigs=" + gigs + ", tracks=" + tracks + "]";
	}
	
}
