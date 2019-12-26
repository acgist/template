package com.acgist.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>utils - 随机数</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class RandomUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtils.class);

	/**
	 * <p>获取随机数工具</p>
	 * 
	 * @return 随机数工具
	 */
	public static final Random random() {
		try {
			return SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("获取随机数工具异常", e);
		}
		return new Random();
	}
	
}
