package com.artisan.radiohere;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

public class ArtistTest {
	@Test
	public void toJson() throws Exception {
		Venue venue = new Venue("Scala", "E1", new Coordinate(1, 2));
		List<Gig> gigs = Collections.singletonList(
				new Gig("Radiohead", "1-1-2001", "Scala", 1, venue));
		List<Track> tracks = new ArrayList<>();
		Artist artist = new Artist(gigs, tracks);
		System.out.println(new JSONObject(artist));
	}
}
