package com.artisan.radiohere;

import static org.junit.Assert.*;

import org.junit.Test;

public class CacherTest {
	@Test
	public void test() {
		System.out.print(new Cacher().get("x"));
		new Cacher().set("x", "y");
	}
}
