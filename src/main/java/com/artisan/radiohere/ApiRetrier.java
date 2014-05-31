package com.artisan.radiohere;

import org.apache.http.client.fluent.Request;

public class ApiRetrier {
	private final int retries;
	private final int msPauseBetween;
	private final String url;
	
	public ApiRetrier(int retries, int msPauseBetween, String url) {
		this.retries = retries;
		this.msPauseBetween = msPauseBetween;
		this.url = url;
	}

	public String execute() throws ApiException {
		for (int i = 0; i < retries; i++) {
			try {
				return callApi();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				try {
					Thread.sleep(msPauseBetween);
				} catch (InterruptedException e1) {}
			}
		}
		return callApi();
	}

	private String callApi() throws ApiException {
		try {
			return Request.Get(url).execute().returnContent().asString();
		} catch (Exception e) {
			throw new ApiException(url, e);
		}
	}
}
