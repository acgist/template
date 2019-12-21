package com.acgist.main;

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
	
	private ApplicationLauncher() {
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
	 * <p>关闭系统</p>
	 * 
	 * @param status 关闭状态
	 */
	public void shutdown(int status) {
		System.exit(status);
	}
	
}
