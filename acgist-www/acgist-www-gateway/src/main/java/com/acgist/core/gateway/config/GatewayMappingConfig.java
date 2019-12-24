package com.acgist.core.gateway.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.acgist.core.gateway.gateway.GatewayMapping;
import com.acgist.core.gateway.gateway.GatewayType;
import com.acgist.core.gateway.request.UserNickRequest;
import com.acgist.core.gateway.request.UserRequest;
import com.acgist.core.gateway.response.UserNickResponse;
import com.acgist.core.gateway.response.UserResponse;

/**
 * <p>config - 网关映射</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
public class GatewayMappingConfig {

	@PostConstruct
	public void config() {
		final var mapping = GatewayMapping.getInstance();
		mapping.register(new GatewayType(true, false, "用户信息查询", "/gateway/user", UserRequest.class, UserResponse.class));
		mapping.register(new GatewayType(true, false, "用户昵称修改", "/gateway/user/nick", UserNickRequest.class, UserNickResponse.class));
	}
	
}
