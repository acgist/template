package com.api.feign.user.service;

import org.springframework.cloud.openfeign.FeignClient;

import com.api.core.config.APIConstApplication;
import com.api.core.user.service.IUserService;
import com.api.feign.config.FeignConfig;
import com.api.feign.user.fallback.UserServiceFallback;

/**
 * 服务调用 - 用户
 */
@FeignClient(name = APIConstApplication.API_SERVICE_USER, configuration = FeignConfig.class, fallback = UserServiceFallback.class)
public interface UserService extends IUserService {

}
