package com.acgist.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.core.gateway.API;
import com.acgist.utils.HTTPUtils;
import com.acgist.utils.URLUtils;

/**
 * HTTP请求工具
 *
 */
public class HTTPUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPUtils.class);
	
	private static final Map<String, String> HEADERS = new HashMap<>();
	
	static {
		HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/666.66 (KHTML, like Gecko) Chrome/66.6.6666.866 Safari/666.66"); // 浏览器
	}
	
	public static final String get(String uri) {
		HttpRequest request = buildGet(uri);
		return request(request);
	}
	
	public static final String post(String uri, String data) {
		HttpRequest request = buildPost(uri, data);
		return request(request);
	}
	
	public static final String post(String uri, Map<String, String> data) {
		HttpRequest request = buildPost(uri, data);
		return request(request);
	}
	
	public static final String request(HttpRequest request) {
		try {
			final HttpClient client = HttpClient.newBuilder()
				.followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofMinutes(10))
				.build();
			final HttpResponse<String> response = client
				.send(
					request,
					BodyHandlers.ofString()
				);
			return response.body();
		} catch (IOException | InterruptedException e) {
			LOGGER.error("请求异常", e);
		}
		return null;
	}
	
	public static final HttpRequest buildGet(String uri) {
		return buildRequestBuilder(uri, null).GET().build();
	}
	
	public static final HttpRequest buildGet(String uri, Map<String, String> headers) {
		return buildRequestBuilder(uri, headers).GET().build();
	}
	
	public static final HttpRequest buildPost(String uri, String data) {
		return buildPost(uri, data, null);
	}
	
	public static final HttpRequest buildPost(String uri, Map<String, String> data) {
		return buildPost(uri, data, null);
	}
	
	public static final HttpRequest buildPost(String uri, String data, Map<String, String> headers) {
		return buildPost(uri, false, data, headers);
	}
	
	public static final HttpRequest buildPost(String uri, Map<String, String> data, Map<String, String> headers) {
		final String dataParams = data.entrySet().stream().map(entity -> {
			return entity.getKey() + "=" + URLUtils.encoding(entity.getValue());
		}).collect(Collectors.joining("&"));
		return buildPost(uri, true, dataParams, headers);
	}
	
	/**
	 * @param uri 请求地址
	 * @param form 是否是表单请求
	 * @param data 请求数据
	 * @param headers 请求头
	 */
	public static final HttpRequest buildPost(String uri, boolean form, String data, Map<String, String> headers) {
		final Builder builder = buildRequestBuilder(uri, headers);
		if(form) {
			builder.header("Content-type", "application/x-www-form-urlencoded;charset=" + API.DEFAULT_CHARSET);
		}
		if(data == null || data.isEmpty()) {
			builder.POST(BodyPublishers.noBody());
		} else {
			builder.POST(BodyPublishers.ofString(data, Charset.forName(API.DEFAULT_CHARSET)));
		}
		return builder.build();
	}
	
	/**
	 * 请求构建器
	 */
	public static final Builder buildRequestBuilder(String uri, Map<String, String> headers) {
		final Builder builder = HttpRequest
			.newBuilder()
			.uri(URI.create(uri));
		if(headers != null && !headers.isEmpty()) {
			headers.forEach((key, value) -> builder.header(key, value));
		}
		if(HEADERS != null && !HEADERS.isEmpty()) {
			HEADERS.forEach((key, value) -> builder.header(key, value));
		}
		return builder;
	}
	
}
