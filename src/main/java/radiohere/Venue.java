package radiohere;

public class Venue {
	private final String name;
	private final String postcode;
	
	public Venue(String name, String postcode) {
		this.name = name;
		this.postcode = postcode;
	}

	public String getName() {
		return name;
	}
	
	public String getPostcode() {
		return postcode;
	}

	@Override
	public String toString() {
		return "Venue [name=" + name + ", postcode=" + postcode + "]";
	}
}
