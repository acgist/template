package com.acgist.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.utils.URLUtils;

/**
 * utils - URL
 */
public class URLUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(URLUtils.class);

	/**
	 * URL编码
	 */
	public static final String encoding(String value) {
		if(value == null) {
			return null;
		}
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL编码异常：{}", value, e);
		}
		return value;
	}
	
	/**
	 * URL解码
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
	
}
