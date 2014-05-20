package radiohere;

public class Track {
	private final String name;
	private final String streamUri;

	public Track(String name, String streamUri) {
		this.name = name;
		this.streamUri = streamUri;
	}
	
	public String getName() {
		return name;
	}
	
	public String getStreamUrl() {
		return streamUri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((streamUri == null) ? 0 : streamUri.hashCode());
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
		if (streamUri == null) {
			if (other.streamUri != null)
				return false;
		} else if (!streamUri.equals(other.streamUri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Track [name=" + name + ", streamUri=" + streamUri + "]";
	}
}
