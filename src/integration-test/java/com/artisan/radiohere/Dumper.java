package com.artisan.radiohere;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func0;
import rx.observables.GroupedObservable;

public class Dumper {
	public static void dumpForTimePeriod(Observable<?> observable, long milliseconds) throws InterruptedException {
		System.out.println("----------------------------------------------iBEGIN");
		Subscription subscription = dumpObservable(observable, () -> "");
		Thread.sleep(milliseconds);
		subscription.unsubscribe();
	}
	
	public static void dump(Observable<?> observable) throws InterruptedException {
		dump(observable, () -> "");
	}

	public static void dump(Observable<?> observable, Func0<String> prefix) throws InterruptedException {
		System.out.println(prefix.call() + "----------------------------------------------BEGIN");
		dumpObservable(observable, prefix);
	}

	public static void dumpWithThreadId(Observable<?> observable) throws InterruptedException {
		dump(observable, () -> "Thread " + Thread.currentThread().getId() + ": ");
	}
	
	private static Subscription dumpObservable(Observable<?> observable, Func0<String> prefix) {
		return observable.finallyDo(() -> System.out.println("----------------------------------------------END\r\n"))
				.subscribe((value) -> dumpValue(value, prefix),
							 (e) -> System.out.println(prefix.call() + " ✖ " + e.getMessage()),
							 () -> System.out.println(prefix.call() + " ‾"));
	}

	private static void dumpValue(Object value, Func0<String> prefix) {
		if (value instanceof Observable) {
			dumpNestedObservable((Observable) value, prefix);
		} else {
			System.out.println(prefix.call() + " • " + value);
		}
	}

	private static Subscription dumpNestedObservable(Observable<?> observable, Func0<String> prefix) {
		return observable.reduce(new ArrayList<String>(), (a, b) -> {
			a.add(toString(b));
			return a;
		}).subscribe((value) -> dumpArrayAsObservable(value));	
	}

	private static String toString(Object value) {
		if (value instanceof GroupedObservable<?, ?>) {
			return "Grouped Observable";
		}
		return value.toString();
	}

	private static void dumpArrayAsObservable(ArrayList<String> values) {
		System.out.println(" |→•" + String.join("→•", values) + "→|");
	}
}