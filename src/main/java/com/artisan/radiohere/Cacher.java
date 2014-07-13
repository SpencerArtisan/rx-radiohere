package com.artisan.radiohere;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class Cacher {
    private Logger logger = Logger.getLogger(this.getClass().getName());
	private JedisPool pool;

	public Cacher() {
	    String redisURIString = System.getenv("REDISCLOUD_URL");
		try {
		    if (redisURIString == null || redisURIString.isEmpty()) {
		    		logger.warning("Redis url not specified");
		    		redisURIString = "redis://rediscloud:6ydltAvSi43mFLYX@pub-redis-19281.eu-west-1-1.1.ec2.garantiadata.com:19281";
		    }
			URI redisURI = new URI(redisURIString);
		    pool = new JedisPool(new JedisPoolConfig(),
		            redisURI.getHost(),
		            redisURI.getPort(),
		            Protocol.DEFAULT_TIMEOUT,
		            redisURI.getUserInfo().split(":",2)[1]);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Redis url invalid - " + redisURIString);
		}		
	}

	public String get(String key) {
		Jedis resource = pool.getResource();
		try {
			return resource.get(key);
		} finally {
			pool.returnResource(resource);
		}
	}
	
	public void set(String key, String value) {
		Jedis resource = pool.getResource();
		try {
			resource.set(key, value);
		} finally {
			pool.returnResource(resource);
		}
	}
}
