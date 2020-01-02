package com.acgist.core.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.core.gateway.interceptor.AdminPermissionInterceptor;
import com.acgist.core.gateway.interceptor.AdminSaveInterceptor;

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
	private AdminPermissionInterceptor adminPermissionInterceptor;
	@Autowired
	private AdminSaveInterceptor adminSaveInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("配置拦截器：adminPermissionInterceptor");
		registry.addInterceptor(this.adminPermissionInterceptor).addPathPatterns("/admin/**");
		LOGGER.info("配置拦截器：adminSaveInterceptor");
		registry.addInterceptor(this.adminSaveInterceptor).addPathPatterns("/admin/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
