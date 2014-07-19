package com.artisan.radiohere;

import java.util.ArrayList;
import java.util.List;


public class Gig {
    private final String artist;
	private final String date;
	private final String venueName;
	private final Integer venueId;
	private final Integer songkickArtistId;
	private final Coordinate venue;
	private List<Track> tracks;

	public Gig(String artist, Integer songkickArtistId, String date, String venueName, Integer venueId, Coordinate venue) {
		this(artist, songkickArtistId, date, venueName, venueId, venue, new ArrayList<>());
	}
	
	public Gig(String artist, Integer songkickArtistId, String date, String venueName, Integer venueId, Coordinate venue, List<Track> tracks) {
		this.artist = artist;
		this.songkickArtistId = songkickArtistId;
		this.date = date;
		this.venueName = venueName;
		this.venueId = venueId;
		this.venue = venue;
		this.tracks = tracks;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public Gig setTracks(List<Track> tracks) {
		return new Gig(artist, songkickArtistId, date, venueName, venueId, venue, tracks);
	}

	public Gig addTrack(Track track) {
		ArrayList<Track> newTracks = new ArrayList<> (tracks);
		newTracks.add(track);
		return new Gig(artist, songkickArtistId, date, venueName, venueId, venue, newTracks);
	}
	
	public String getArtist() {
		return artist;
	}

	public Integer getSongkickArtistId() {
		return songkickArtistId;
	}

	public ArtistId getArtistId() {
		return new ArtistId(songkickArtistId, artist);
	}
	
	public String getDate() {
		return date;
	}

	public String getVenueName() {
		return venueName;
	}

	public Integer getVenueId() {
		return venueId;
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

	public boolean isVenueWithinKm(Coordinate origin, double km) {
		return getVenueDistance(origin) < km;
	}

	public Double getVenueDistance(Coordinate origin) {
		return venue == null ? null : origin.kmFrom(venue);
	}

	public Double getDistance() {
		return getVenueDistance(Coordinate.YEATE_STREET);
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
				+ venueName + ", venueId=" + venueId + ", songkickArtistId="
				+ songkickArtistId + ", venue=" + venue + ", tracks=" + tracks
				+ "]";
	}

}

class ArtistId {
	final private Integer id;
	final private String name;
	
	public ArtistId(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ArtistId other = (ArtistId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
