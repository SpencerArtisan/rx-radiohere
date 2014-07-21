package com.artisan.radiohere;

import java.util.logging.Logger;

import org.apache.http.client.fluent.Request;

public class ApiRetrier {
    private Logger logger = Logger.getLogger(this.getClass().getName());
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
				logger.warning(e.getMessage());
				try {
					Thread.sleep(msPauseBetween);
				} catch (InterruptedException e1) {
					logger.warning("!!!! Exception during sleep: " + e1.getMessage());
				}
			}
		}
		return callApi();
	}

	private String callApi() throws ApiException {
		try {
			logger.info("Api call starting - " + url);
			long start = System.currentTimeMillis();
			String result = Request.Get(url).execute().returnContent().asString();
			long duration = System.currentTimeMillis() - start;
			logger.info("Api call took " + duration + "ms - " + url);
			return result;
		} catch (Exception e) {
			logger.warning("Problem calling url " + url);
			throw new ApiException(url, e);
		}
	}
}
