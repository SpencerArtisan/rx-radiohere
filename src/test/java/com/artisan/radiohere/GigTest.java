package com.artisan.radiohere;

import org.json.JSONObject;
import org.junit.Test;

public class GigTest {
	@Test
	public void toJson() throws Exception {
		Venue venue = new Venue("Scala", "E1", new Coordinate(51.5, -0.8));
		Gig gig = new Gig("Radiohead", 1, "1-1-2001", "Scala", 1, venue);
		System.out.println(new JSONObject(gig));
	}
}

