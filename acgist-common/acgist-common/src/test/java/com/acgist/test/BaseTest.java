package com.acgist.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class BaseTest {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * <p>消耗时间计算</p>
	 */
	protected AtomicLong cos = new AtomicLong();
	
	/**
	 * <p>设置日志级别</p>
	 */
	protected void info() {
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.getLoggerList().forEach(logger -> {
			logger.setLevel(Level.INFO);
		});
	}
	
	/**
	 * <p>记录日志</p>
	 * 
	 * @param log 日志信息
	 */
	protected void log(Object log) {
		LOGGER.info("{}", log);
	}
	
	/**
	 * <p>阻止自动关闭</p>
	 * 
	 * @throws InterruptedException 线程中断
	 */
	protected void pause() throws InterruptedException {
		Thread.sleep(Long.MAX_VALUE);
	}
	
	/**
	 * <p>开始计算消耗</p>
	 */
	protected void cost() {
		this.cos.set(System.currentTimeMillis());
	}
	
	/**
	 * <p>计算消耗</p>
	 */
	protected void costed() {
		final long time = System.currentTimeMillis();
		final long cos = time - this.cos.getAndSet(time);
		this.LOGGER.info("消耗时间：毫秒：{}，秒：{}", cos, cos / 1000);
	}
	
	/**
	 * <p>计算消耗</p>
	 * 
	 * @param count 任务数量
	 * @param thread 线程数量
	 * @param function 任务
	 * 
	 * @throws InterruptedException 线程中断
	 */
	protected void cost(int count, int thread, Function<Void, Void> function) throws InterruptedException {
		this.cost();
		final CountDownLatch latch = new CountDownLatch(count);
		final var executor = Executors.newFixedThreadPool(thread);
		for (int index = 0; index < count; index++) {
			executor.submit(() -> {
				try {
					function.apply(null);
				} catch (Exception e) {
					this.LOGGER.error("执行异常", e);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
		this.costed();
	}
	
}
