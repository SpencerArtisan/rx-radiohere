package com.artisan.radiohere;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class GigTest {
	@Test
	public void toJson() throws Exception {
		Gig gig = new Gig("Radiohead", "1-1-2001", "Scala", new Coordinate(51.5, -0.8));
		System.out.println(new JSONObject(gig));
	}
	
	@Test
	public void setDistance() throws Exception {
		Gig gig = new Gig("Radiohead", "1-1-2001", "Scala", Coordinate.YEATE_STREET);
		Gig updatedGig = gig.setDistance(1.2);
		assertThat(gig.getDistance(), is(nullValue()));
		assertThat(updatedGig.getDistance(), is(1.2));
	}
}

