package com.artisan.radiohere;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Test;

public class GigTest {
	private Gig gig = new Gig("Radiohead", "1-1-2001", "Scala", new Coordinate(51.5, -0.8), "songkick url");
	private Track track = new Track("a track", "a url");
	
	@Test
	public void toJson() throws Exception {
		System.out.println(new JSONObject(gig));
	}
	
	@Test
	public void initialDistanceIsNull() throws Exception {
		assertThat(gig.getDistance(), is(nullValue()));
	}
	
	@Test
	public void setDistanceReturnsUpdatedGig() throws Exception {
		Gig updatedGig = gig.setDistance(1.2);
		assertThat(updatedGig.getDistance(), is(1.2));
	}
	
	@Test
	public void setDistanceDoesntChangeOriginalGig() throws Exception {
		gig.setDistance(1.2);
		assertThat(gig.getDistance(), is(nullValue()));
	}

	@Test
	public void addTrackReturnsUpdatedGig() throws Exception {
		Gig updatedGig = gig.addTrack(track);
		assertThat(updatedGig.getTracks(), contains(track));
	}
	
	@Test
	public void addTrackDoesntChangeOriginalGig() throws Exception {
		gig.addTrack(track);
		assertThat(gig.getTracks(), is(empty()));
	}
	
	@Test
	public void isDistanceWithinWhenItIs() throws Exception {
		Gig updatedGig = gig.setDistance(1.2);
		assertThat(updatedGig.isDistanceWithinKm(1.3), is(true));
	}
	
	@Test
	public void isDistanceWithinWhenItIsnt() throws Exception {
		Gig updatedGig = gig.setDistance(1.2);
		assertThat(updatedGig.isDistanceWithinKm(1.1), is(false));
	}

	@Test
	public void isDistanceWithinWhenItIsExactly() throws Exception {
		Gig updatedGig = gig.setDistance(1.2);
		assertThat(updatedGig.isDistanceWithinKm(1.2), is(true));
	}
}
