package com.api.core.config;

/**
 * config - URL
 */
public interface APIConstURL {

	/**
	 * 服务前缀
	 */
	String URL_SERVICE = "/service";
	
	/**
	 * 熔断前缀
	 */
	String URL_FALLBACK = "/fallback";
	
	/**
	 * 熔断前缀
	 */
	String URL_FALLBACK_SERVICE = URL_FALLBACK + URL_SERVICE;
	
	/**
	 * 网关前缀
	 */
	String URL_GATEWAY = "/gateway/api";
	
}
