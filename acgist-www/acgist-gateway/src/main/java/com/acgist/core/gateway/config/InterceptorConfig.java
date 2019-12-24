package com.acgist.core.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.core.gateway.interceptor.GatewayInteceptor;
import com.acgist.core.gateway.interceptor.VerifyInteceptor;

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
	private GatewayInteceptor gatewayInteceptor;
	@Autowired
	private VerifyInteceptor verifyInteceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("配置拦截器：gatewayInteceptor");
		registry.addInterceptor(this.gatewayInteceptor).addPathPatterns("/gateway/**");
		LOGGER.info("配置拦截器：verifyInteceptor");
		registry.addInterceptor(this.verifyInteceptor).addPathPatterns("/gateway/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
