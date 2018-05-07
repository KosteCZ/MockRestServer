package com.dixonscarphone.webserver.shared;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

	private AtomicInteger count = new AtomicInteger();

	public Counter() {
		//count.incrementAndGet();
	}

	public int getCount() {
		return count.get();
	}

	public int incrementCount() {
		return count.incrementAndGet();
	}

}
