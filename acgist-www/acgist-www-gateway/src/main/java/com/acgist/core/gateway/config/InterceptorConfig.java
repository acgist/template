package com.acgist.core.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.core.interceptor.GatewayAuthoInterceptor;
import com.acgist.core.interceptor.GatewayInteceptor;
import com.acgist.core.interceptor.GatewayPermissionInteceptor;
import com.acgist.core.interceptor.GatewaySaveInteceptor;
import com.acgist.core.interceptor.GatewaySignatureInteceptor;
import com.acgist.core.interceptor.GatewayTimeInteceptor;
import com.acgist.core.interceptor.GatewayVerifyInteceptor;

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
	private GatewayVerifyInteceptor gatewayVerifyInteceptor;
	@Autowired
	private GatewayTimeInteceptor gatewayTimeInteceptor;
	@Autowired
	private GatewayAuthoInterceptor gatewayAuthoInterceptor;
	@Autowired
	private GatewaySignatureInteceptor gatewaySignatureInteceptor;
	@Autowired
	private GatewayPermissionInteceptor gatewayPermissionInteceptor;
	@Autowired
	private GatewaySaveInteceptor gatewaySaveInteceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("配置拦截器：gatewayInteceptor");
		registry.addInterceptor(this.gatewayInteceptor).addPathPatterns("/gateway/**");
		LOGGER.info("配置拦截器：gatewayVerifyInteceptor");
		registry.addInterceptor(this.gatewayVerifyInteceptor).addPathPatterns("/gateway/**");
		LOGGER.info("配置拦截器：gatewayTimeInteceptor");
		registry.addInterceptor(this.gatewayTimeInteceptor).addPathPatterns("/gateway/**");
		LOGGER.info("配置拦截器：gatewayAuthoInterceptor");
		registry.addInterceptor(this.gatewayAuthoInterceptor).addPathPatterns("/gateway/**");
		LOGGER.info("配置拦截器：gatewaySignatureInteceptor");
		registry.addInterceptor(this.gatewaySignatureInteceptor).addPathPatterns("/gateway/**");
		LOGGER.info("配置拦截器：gatewayPermissionInteceptor");
		registry.addInterceptor(this.gatewayPermissionInteceptor).addPathPatterns("/gateway/**");
		LOGGER.info("配置拦截器：gatewaySaveInteceptor");
		registry.addInterceptor(this.gatewaySaveInteceptor).addPathPatterns("/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
