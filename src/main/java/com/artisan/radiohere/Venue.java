package com.artisan.radiohere;

public class Venue {
	private final String name;
	private final String postcode;
	private final Coordinate coordinate;
	
	public Venue(String name, String postcode, Coordinate coordinate) {
		this.name = name;
		this.postcode = postcode;
		this.coordinate = coordinate;
	}

	public String getName() {
		return name;
	}
	
	public String getPostcode() {
		return postcode;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	@Override
	public String toString() {
		return "Venue [name=" + name + ", postcode=" + postcode
				+ ", coordinate=" + coordinate + "]";
	}
}
