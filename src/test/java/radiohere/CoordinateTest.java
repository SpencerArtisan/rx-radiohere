package radiohere;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class CoordinateTest {
	@Test
	public void distanceBetweenSamePoint() throws Exception {
		Coordinate point = new Coordinate(51.0, 1.0);
		assertThat(point.kmFrom(point), is(0.0));
	}
	
	@Test
	public void distanceBetweenPoints() throws Exception {
		Coordinate point1 = new Coordinate(51.0, 1.0);
		Coordinate point2 = new Coordinate(52.0, 2.0);
		assertThat(point1.kmFrom(point2), closeTo(130.95, 0.01));
	}
}
