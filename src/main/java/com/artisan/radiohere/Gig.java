package com.artisan.radiohere;

import java.util.ArrayList;
import java.util.List;

public class Gig {
    private final String artist;
	private final String date;
	private final String venueName;
	private final Coordinate venue;
	private final Double distance;
	private final String songkickUrl;
	private final List<Track> tracks;

	public Gig(String artist, String date, String venueName, Coordinate venue, String songkickUrl) {
		this(artist, date, venueName, venue, null, songkickUrl, new ArrayList<>());
	}
	
	public Gig(String artist, String date, String venueName, Coordinate venue, Double distance, String songkickUrl, List<Track> tracks) {
		this.artist = artist;
		this.date = date;
		this.venueName = venueName;
		this.venue = venue;
		this.songkickUrl = songkickUrl;
		this.tracks = tracks;
		this.distance = distance;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public Gig addTrack(Track track) {
		ArrayList<Track> newTracks = new ArrayList<> (tracks);
		newTracks.add(track);
		return new Gig(artist, date, venueName, venue, distance, songkickUrl, newTracks);
	}
	
	public String getArtist() {
		return artist;
	}

	public String getDate() {
		return date;
	}

	public String getVenueName() {
		return venueName;
	}

	public String getSongkickUrl() {
		return songkickUrl;
	}

	public boolean hasArtist() {
		return artist != null;
	}

	public boolean hasVenue() {
		return venue != null;
	}

	public Coordinate getVenue() {
		return venue;
	}

	public boolean isDistanceWithinKm(double km) {
		return distance <= km;
	}

	public Gig setDistance(double distance) {
		return new Gig(artist, date, venueName, venue, distance, songkickUrl, tracks);
	}

	public Double getDistance() {
		return distance;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gig other = (Gig) obj;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Gig [artist=" + artist + ", date=" + date + ", venueName="
				+ venueName + ", venue=" + venue + ", tracks=" + tracks
				+ ", distance=" + distance + ", songkickUrl=" + songkickUrl + "]";
	}
}
