package com.acgist.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.core.config.AcgistConst;

/**
 * <p>utils - URL</p>
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
	
}
