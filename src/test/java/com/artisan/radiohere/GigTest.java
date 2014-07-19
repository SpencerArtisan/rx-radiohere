package com.artisan.radiohere;

import org.json.JSONObject;
import org.junit.Test;

public class GigTest {
	@Test
	public void toJson() throws Exception {
		Gig gig = new Gig("Radiohead", "1-1-2001", "Scala", new Coordinate(51.5, -0.8));
		System.out.println(new JSONObject(gig));
	}
}

