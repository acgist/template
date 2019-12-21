package com.acgist.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * <p>启动器</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class ApplicationLauncher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLauncher.class);
	
	/**
	 * <p>启动锁</p>
	 */
	private final Object lock;
	
	private ApplicationLauncher() {
		this.lock = new Object();
	}

	public static final ApplicationLauncher newInstance() {
		return new ApplicationLauncher();
	}
	
	/**
	 * <p>Web启动</p>
	 * 
	 * @param args 参数
	 * @param clazz 启动类
	 * 
	 * @return 启动器
	 */
	public ApplicationLauncher web(String[] args, Class<?> clazz) {
		SpringApplication.run(clazz, args);
		return this;
	}
	
	/**
	 * <p>非Web启动</p>
	 * 
	 * @param args 参数
	 * @param clazz 启动类
	 * 
	 * @return 启动器
	 */
	public ApplicationLauncher run(String[] args, Class<?> clazz) {
		final var application = new SpringApplicationBuilder(clazz)
		    .web(WebApplicationType.NONE)
		    .build();
	    application.run(args);
	    return this;
	}

	/**
	 * <p>锁定</p>
	 */
	public void lock() {
		try {
			synchronized (this.lock) {
				this.lock.wait(Long.MAX_VALUE);
			}
		} catch (InterruptedException e) {
			LOGGER.error("等待异常", e);
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * <p>解锁</p>
	 */
	public void unlock() {
		synchronized (this.lock) {
			this.lock.notify();
		}
	}
	
	/**
	 * <p>关闭系统</p>
	 * 
	 * @param status 关闭状态
	 */
	public void shutdown(int status) {
		System.exit(status);
	}
	
}
