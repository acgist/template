package com.acgist.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.acgist.utils.UuidUtils;

/**
 * <p>启动器</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class ApplicationLauncher {
	
	private static final ApplicationLauncher INSTANCE = new ApplicationLauncher();
	
	/**
	 * <p>实例ID</p>
	 */
	public static final String APPLICATION_INSTANCE_ID = "acgist.id";
	
	/**
	 * <p>实例ID</p>
	 */
	private final String id;
	
	private ApplicationLauncher() {
		this.id = UuidUtils.buildUuid().toUpperCase();
		System.setProperty(APPLICATION_INSTANCE_ID, this.id);
	}

	public static final ApplicationLauncher getInstance() {
		return INSTANCE;
	}
	
	/**
	 * <p>获取实例ID</p>
	 * 
	 * @return 实例ID
	 */
	public String id() {
		return this.id;
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
