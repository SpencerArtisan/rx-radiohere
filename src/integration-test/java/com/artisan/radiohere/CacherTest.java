package com.artisan.radiohere;

import org.junit.Test;

public class CacherTest {
	@Test
	public void test() {
		System.out.print(new Cacher().get("x"));
		new Cacher().set("xx", "y");
	}
}
