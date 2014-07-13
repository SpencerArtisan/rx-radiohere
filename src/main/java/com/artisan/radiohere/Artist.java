package com.artisan.radiohere;

import java.util.List;
import java.util.logging.Logger;

public class Artist {
    private Logger logger = Logger.getLogger(this.getClass().getName());
	private final Gig gig;
	private final List<Track> tracks;

	public Artist(Gig gig, List<Track> tracks) {
		this.gig = gig;
		this.tracks = tracks;
		logger.info("Created Artist " + this);
	}

	public Gig getGig() {
		return gig;
	}

	public List<Track> getTracks() {
		return tracks;
	}
	
	public String getName() {
		return gig.getArtist();
	}
	
	@Override
	public String toString() {
		return "Artist [name=" + getName() + ", gig=" + gig + ", tracks=" + tracks + "]";
	}
}
