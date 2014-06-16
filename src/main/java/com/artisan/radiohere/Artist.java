package com.artisan.radiohere;

import java.util.List;
import java.util.logging.Logger;

public class Artist {
    private Logger logger = Logger.getLogger(this.getClass().getName());
	private final List<Gig> gigs;
	private final List<Track> tracks;

	public Artist(List<Gig> gigs, List<Track> tracks) {
		this.gigs = gigs;
		this.tracks = tracks;
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
	
	@Override
	public String toString() {
		return "Artist [name=" + getName() + ", gigs=" + gigs + ", tracks=" + tracks + "]";
	}
}
