package com.acgist.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.core.interceptor.CsrfInterceptor;
import com.acgist.core.interceptor.UserInterceptor;

/**
 * <p>config - 拦截器</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConfig.class);
	
	@Autowired
	private CsrfInterceptor csrfInterceptor;
	@Autowired
	private UserInterceptor userInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("配置拦截器：csrfInterceptor");
		registry.addInterceptor(this.csrfInterceptor).addPathPatterns("/**");
		LOGGER.info("配置拦截器：userInterceptor");
		registry.addInterceptor(this.userInterceptor).addPathPatterns("/user/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
