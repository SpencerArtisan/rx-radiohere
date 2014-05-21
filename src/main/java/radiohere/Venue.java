package radiohere;

public class Venue {
	private final String name;
	private final String postcode;
	private final double latitude;
	private final double longitude;
	
	public Venue(String name, String postcode, double latitude, double longitude) {
		this.name = name;
		this.postcode = postcode;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}
	
	public String getPostcode() {
		return postcode;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "Venue [name=" + name + ", postcode=" + postcode + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
}
