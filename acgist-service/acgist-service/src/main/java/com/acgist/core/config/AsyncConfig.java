package com.acgist.core.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>config - 异步线程线程池配置</p>
 */
@Configuration
public class AsyncConfig {

	@Bean
	public Executor taskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(100); // 最大线程
		executor.setCorePoolSize(4); // 最少线程
		executor.setQueueCapacity(100); // 缓冲队列（缓冲队列未满不会增加线程）
		executor.setKeepAliveSeconds(60); // 线程空闲时间
		executor.setThreadNamePrefix("ACGIST-ASYNC-");
		executor.initialize();
		return executor;
	}

}
