package com.artisan.radiohere;

public class Track {
	private final String name;
	private final String streamUrl;

	public Track(String name, String streamUrl) {
		this.name = name;
		this.streamUrl = streamUrl;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasStreamUrl() {
		return streamUrl != null;
	}
	
	public String getStreamUrl() {
		return streamUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((streamUrl == null) ? 0 : streamUrl.hashCode());
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
		Track other = (Track) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (streamUrl == null) {
			if (other.streamUrl != null)
				return false;
		} else if (!streamUrl.equals(other.streamUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Track [name=" + name + ", streamUri=" + streamUrl + "]";
	}
}
