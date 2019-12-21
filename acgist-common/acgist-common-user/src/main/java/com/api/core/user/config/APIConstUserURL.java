package com.api.core.user.config;

import com.api.core.config.APIConstURL;

/**
 * config - URL - 用户
 */
public interface APIConstUserURL extends APIConstURL {

	// 用户
	String URL_USER = "/user";
	
	// 用户登陆
	String URL_USER_LOGIN = URL_USER + "/login";
	String URL_SERVICE_USER_LOGIN = URL_SERVICE + URL_USER_LOGIN;
	
	// 获取用户鉴权信息
	String URL_USER_AUTHO = URL_USER + "/autho";
	String URL_SERVICE_USER_AUTHO = URL_SERVICE + URL_USER_AUTHO;
	
}
