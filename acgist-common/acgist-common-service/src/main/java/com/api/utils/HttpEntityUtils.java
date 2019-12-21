package com.api.utils;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * utils - HttpEntity
 */
public class HttpEntityUtils {

	/**
	 * 表单请求
	 */
	public static final HttpEntity<MultiValueMap<String, Object>> formEntity(Map<String, Object> map) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final MultiValueMap<String, Object> values = new LinkedMultiValueMap<>();
		map.forEach((key, value) -> {
			values.add(key, value);
		});
		return new HttpEntity<MultiValueMap<String, Object>>(values, headers);
	}
	
	/**
	 * JSON请求数据
	 */
	public static final HttpEntity<Object> jsonEntity(Object object) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new HttpEntity<Object>(object, headers);
	}
	
}
