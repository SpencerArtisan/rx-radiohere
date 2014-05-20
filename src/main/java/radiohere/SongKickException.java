package radiohere;

public class SongKickException extends Exception {
	public SongKickException(Throwable cause) {
		super("Problem calling SongKick", cause);
	}
}
