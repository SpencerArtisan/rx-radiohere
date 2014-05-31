package com.artisan.radiohere;

import java.util.List;

public class Artist {
	private final List<Gig> gigs;
	private final List<Track> tracks;
	private final ArtistGenre genre;

	public Artist(List<Gig> gigs, List<Track> tracks) {
		this(gigs, tracks, null);
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
		return new Artist(gigs, tracks, artistGenre);
	}

	@Override
	public String toString() {
		return "Artist [name=" + getName() + ", genre=" + genre
				+ ", gigs=" + gigs + ", tracks=" + tracks + "]";
	}
	
}
