package radiohere;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import net.avh4.test.junit.Nested;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;

@RunWith(Nested.class)
public class GigWithTracksObservableFactoryTest {
	@Mock
	private GigObservableFactory gigObservableFactory;
	
	@Mock
	private TrackObservableFactory trackObservableFactory;

	private GigWithTracksObservableFactory gigWithTracksObservableFactory;
	private Observable<GigWithTracks> gigWithTracksObservable;
	private List<GigWithTracks> gigWithTracks;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		gigWithTracksObservableFactory = new GigWithTracksObservableFactory(gigObservableFactory, trackObservableFactory);
	}
	
	public class WithNoGigs {
		@Before
		public void before() {
			when(gigObservableFactory.create()).thenReturn(Observable.empty());
			gigWithTracksObservable = gigWithTracksObservableFactory.create();
			gigWithTracks = gigWithTracksObservable.toList().toBlockingObservable().single();			
		}
		
		@Test
		public void shouldBeNoItems() throws Exception {
			assertThat(gigWithTracks, empty());
		}
	}
	
	public class WithOneGigAndNoTracks {
		private Gig gig;

		@Before
		public void before() {
			gig = new Gig("artist", "date", "venue", 42, null);
			when(gigObservableFactory.create()).thenReturn(Observable.just(gig));
			when(trackObservableFactory.create("artist")).thenReturn(Observable.empty());
			gigWithTracksObservable = gigWithTracksObservableFactory.create();			
			gigWithTracks = gigWithTracksObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeOneItem() throws Exception {
			assertThat(gigWithTracks.size(), is(1));
		}
		
		@Test
		public void shouldHaveTheGigDetails() throws Exception {
			assertThat(gigWithTracks.get(0).getGig(), equalTo(gig));
		}
		
		@Test
		public void shouldHaveNoTracks() throws Exception {
			assertThat(gigWithTracks.get(0).getTracks(), empty());
		}
	}

	public class WithOneGigAndOneTrack {
		private Gig gig;
		private Track track;
		
		@Before
		public void before() {
			gig = new Gig("artist", "date", "venue", 42, null);
			track = new Track("name", "streamurl");
			when(gigObservableFactory.create()).thenReturn(Observable.just(gig));
			when(trackObservableFactory.create("artist")).thenReturn(Observable.just(track));
			gigWithTracksObservable = gigWithTracksObservableFactory.create();			
			gigWithTracks = gigWithTracksObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeTwoItems() throws Exception {
			assertThat(gigWithTracks.size(), is(2));
		}
		
		@Test
		public void firstItemShouldHaveTheGigDetails() throws Exception {
			assertThat(gigWithTracks.get(0).getGig(), equalTo(gig));
		}
		
		@Test
		public void firstItemShouldHaveNoTracks() throws Exception {
			assertThat(gigWithTracks.get(0).getTracks(), empty());
		}
		
		@Test
		public void secondItemShouldHaveTheGigDetails() throws Exception {
			assertThat(gigWithTracks.get(1).getGig(), equalTo(gig));
		}
		
		@Test
		public void secondItemShouldHaveOneTrack() throws Exception {
			assertThat(gigWithTracks.get(1).getTracks().size(), is(1));
		}

		@Test
		public void secondItemShouldHaveTheTrackDetails() throws Exception {
			assertThat(gigWithTracks.get(1).getTracks(), contains(track));
		}
	}
	
	public class WithOneGigAndTwoTracks {
		private Gig gig;
		private Track track1;
		private Track track2;
		
		@Before
		public void before() {
			gig = new Gig("artist", "date", "venue", 42, null);
			track1 = new Track("name1", "streamurl1");
			track2 = new Track("name2", "streamurl2");
			when(gigObservableFactory.create()).thenReturn(Observable.just(gig));
			when(trackObservableFactory.create("artist")).thenReturn(Observable.from(track1, track2));
			gigWithTracksObservable = gigWithTracksObservableFactory.create();			
			gigWithTracks = gigWithTracksObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeTwoItems() throws Exception {
			assertThat(gigWithTracks.size(), is(2));
		}
		
		@Test
		public void firstItemShouldHaveTheGigDetails() throws Exception {
			assertThat(gigWithTracks.get(0).getGig(), equalTo(gig));
		}
		
		@Test
		public void firstItemShouldHaveNoTracks() throws Exception {
			assertThat(gigWithTracks.get(0).getTracks(), empty());
		}
		
		@Test
		public void secondItemShouldHaveTheGigDetails() throws Exception {
			assertThat(gigWithTracks.get(1).getGig(), equalTo(gig));
		}
		
		@Test
		public void secondItemShouldHaveTwoTracks() throws Exception {
			assertThat(gigWithTracks.get(1).getTracks().size(), is(2));
		}
		
		@Test
		public void secondItemShouldHaveTheTrackDetails() throws Exception {
			assertThat(gigWithTracks.get(1).getTracks(), contains(track1, track2));
		}
	}
	
	public class WithTwoGigsAndOneTrackEach {
		private Gig gig1;
		private Gig gig2;
		private Track track1;
		private Track track2;
		
		@Before
		public void before() {
			gig1 = new Gig("artist1", "date1", "venue1", 42, null);
			gig2 = new Gig("artist2", "date2", "venue2", 43, null);
			track1 = new Track("name1", "streamurl1");
			track2 = new Track("name2", "streamurl2");
			when(gigObservableFactory.create()).thenReturn(Observable.from(gig1, gig2));
			when(trackObservableFactory.create("artist1")).thenReturn(Observable.just(track1));
			when(trackObservableFactory.create("artist2")).thenReturn(Observable.just(track2));
			gigWithTracksObservable = gigWithTracksObservableFactory.create();			
			gigWithTracks = gigWithTracksObservable.toList().toBlockingObservable().single();
		}
		
		@Test
		public void shouldBeFourItems() throws Exception {
			assertThat(gigWithTracks.size(), is(4));
		}
		
		@Test
		public void firstItemShouldHaveTheFirstGigDetails() throws Exception {
			assertThat(gigWithTracks.get(0).getGig(), equalTo(gig1));
		}
		
		@Test
		public void firstItemShouldHaveNoTracks() throws Exception {
			assertThat(gigWithTracks.get(0).getTracks(), empty());
		}
		
		@Test
		public void secondItemShouldHaveTheFirstGigDetails() throws Exception {
			assertThat(gigWithTracks.get(1).getGig(), equalTo(gig1));
		}
		
		@Test
		public void secondItemShouldHaveOneTrack() throws Exception {
			assertThat(gigWithTracks.get(1).getTracks().size(), is(1));
		}

		@Test
		public void secondItemShouldHaveTheFirstTrackDetails() throws Exception {
			assertThat(gigWithTracks.get(1).getTracks(), contains(track1));
		}
		
		@Test
		public void thirdItemShouldHaveTheSecondGigDetails() throws Exception {
			assertThat(gigWithTracks.get(2).getGig(), equalTo(gig2));
		}
		
		@Test
		public void thirdItemShouldHaveNoTracks() throws Exception {
			assertThat(gigWithTracks.get(2).getTracks(), empty());
		}
		
		@Test
		public void fourthItemShouldHaveTheSecondGigDetails() throws Exception {
			assertThat(gigWithTracks.get(3).getGig(), equalTo(gig2));
		}
		
		@Test
		public void fourthItemShouldHaveOneTrack() throws Exception {
			assertThat(gigWithTracks.get(3).getTracks().size(), is(1));
		}
		
		@Test
		public void fourthItemShouldHaveTheSecondTrackDetails() throws Exception {
			assertThat(gigWithTracks.get(3).getTracks(), contains(track2));
		}
	}
}
