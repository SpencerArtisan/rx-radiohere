package radiohere;

public class Gig {
	private final String artist;
	private final String date;
	private final String venueName;
	private final Integer venueId;
	private final Venue venue;

	public Gig(String artist, String date, String venueName, Integer venueId, Venue venue) {
		this.artist = artist;
		this.date = date;
		this.venueName = venueName;
		this.venueId = venueId;
		this.venue = venue;
	}

	public Gig addVenue(Venue venue) {
		return new Gig(artist, date, venueName, venueId, venue);
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

	public Integer getVenueId() {
		return venueId;
	}

	public Venue getVenue() {
		return venue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((getVenue() == null) ? 0 : getVenue().hashCode());
		result = prime * result + venueId;
		result = prime * result
				+ ((venueName == null) ? 0 : venueName.hashCode());
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
		if (getVenue() == null) {
			if (other.getVenue() != null)
				return false;
		} else if (!getVenue().equals(other.getVenue()))
			return false;
		if (venueId != other.venueId)
			return false;
		if (venueName == null) {
			if (other.venueName != null)
				return false;
		} else if (!venueName.equals(other.venueName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Gig [artist=" + artist + ", date=" + date + ", venueName="
				+ venueName + ", venueId=" + venueId + ", venue=" + getVenue() + "]";
	}

}
