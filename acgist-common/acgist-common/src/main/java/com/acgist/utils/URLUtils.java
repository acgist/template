package com.acgist.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.core.config.AcgistConst;

/**
 * <p>utils - URL</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class URLUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(URLUtils.class);

	/**
	 * <p>URL编码</p>
	 * 
	 * @param 原始内容
	 * 
	 * @return 编码内容
	 */
	public static final String encoding(String value) {
		if (value == null) {
			return null;
		}
		try {
			return URLEncoder.encode(value, AcgistConst.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL编码异常：{}", value, e);
		}
		return value;
	}
	
	/**
	 * <p>URL解码</p>
	 * 
	 * @param 编码内容
	 * 
	 * @return 原始内容
	 */
	public static final String decoding(String value) {
		if(value == null) {
			return null;
		}
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL解码异常：{}", value, e);
		}
		return value;
	}
	
	/**
	 * <p>判断请求地址是否是内网地址</p>
	 * 
	 * @param requestUrl 请求地址
	 * 
	 * @return 是否是内网地址
	 */
	public static final boolean isLocal(String requestUrl) {
		if(StringUtils.isEmpty(requestUrl)) {
			return true;
		}
		try {
			final URL url = new URL(requestUrl);
			final String host = url.getHost();
			final var address = InetAddress.getByName(host);
			return
				address.isAnyLocalAddress() ||
				address.isLinkLocalAddress() ||
				address.isLoopbackAddress() ||
				address.isSiteLocalAddress();
		} catch (MalformedURLException | UnknownHostException e) {
			e.printStackTrace();
			return true;
		}
	}
	
}
