package radiohere;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GigWithTracks {
	private final Gig gig;
	private final List<Track> tracks;

	public GigWithTracks(Gig gig) {
		this(gig, new ArrayList<>());
	}

	private GigWithTracks(Gig gig, List<Track> tracks) {
		this.gig = gig;
		this.tracks = tracks;
	}

	public GigWithTracks add(Track track) {
		List<Track> newTracks = new ArrayList<>(tracks);
		newTracks.add(track);
		return new GigWithTracks(gig, newTracks);
	}
	
	public Gig getGig() {
		return gig;
	}

	public List<Track> getTracks() {
		return Collections.unmodifiableList(tracks);
	}
}
