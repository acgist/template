package com.acgist.core.www.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>config - 静态资源</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesConfig.class);
	
	@Value("${acgist.html.path}")
	private String htmlPath;
	@Value("${acgist.static.path}")
	private String staticPath;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		LOGGER.info("静态页面路径：{}", this.staticPath);
		registry.addResourceHandler("*.html").addResourceLocations(this.htmlPath);
		LOGGER.info("静态资源路径：{}", this.staticPath);
		registry.addResourceHandler("/resources/**").addResourceLocations(this.staticPath);
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
	
}
