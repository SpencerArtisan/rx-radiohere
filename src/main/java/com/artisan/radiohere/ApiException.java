package com.artisan.radiohere;

public class ApiException extends Exception {
	public ApiException(String url, Throwable cause) {
		super("Problem calling " + url + " - " + cause.getMessage(), cause);
	}
}
