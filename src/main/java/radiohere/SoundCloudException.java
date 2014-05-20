package radiohere;

public class SoundCloudException extends Exception {
	public SoundCloudException(Throwable cause) {
		super("Problem calling SoundCloud - " + cause.getMessage(), cause);
	}
}
