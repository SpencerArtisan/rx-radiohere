package com.artisan.radiohere;

public class EchoNestException extends Exception {
	public EchoNestException(Throwable cause) {
		super("Problem calling EchoNest - " + cause.getMessage(), cause);
	}
}
