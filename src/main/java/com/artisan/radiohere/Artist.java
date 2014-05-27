package com.artisan.radiohere;

import java.util.List;

public class Artist {
	private final List<Gig> gigs;
	private final List<Track> tracks;

	public Artist(List<Gig> gigs, List<Track> tracks) {
		this.gigs = gigs;
		this.tracks = tracks;
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

	@Override
	public String toString() {
		return "Artist [gigs=" + gigs + ", tracks=" + tracks + "]";
	}
}
