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
	public String toString() {
		return "Track [name=" + name + ", streamUri=" + streamUri + "]";
	}
}
