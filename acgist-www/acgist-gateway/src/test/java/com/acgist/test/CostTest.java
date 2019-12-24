package com.acgist.test;

import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.acgist.core.HTTPClient;

public class CostTest {

	@Test
	public void cost() throws InterruptedException {
		final var size = 10000;
		final var begin = System.currentTimeMillis();
		final var pool = Executors.newFixedThreadPool(50);
		final var count = new CountDownLatch(size);
		for (int i = 0; i < size; i++) {
			pool.submit(() -> {
				try {
					HTTPClient.get("http://localhost:28800/gateway/user", BodyHandlers.ofString());
				} catch (Exception e) {
				} finally {
					count.countDown();
				}
			});
		}
		count.await();
		final var end = System.currentTimeMillis();
		System.out.println(end - begin);
	}
	
}
