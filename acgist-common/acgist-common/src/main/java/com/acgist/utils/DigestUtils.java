package com.acgist.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>散列算法工具</p>
 * <p>散列算法：计算数据摘要</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class DigestUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DigestUtils.class);
	
	/**
	 * <p>散列算法：{@value}</p>
	 */
	public static final String ALGO_MD5 = "MD5";
	/**
	 * <p>散列算法：{@value}</p>
	 */
	public static final String ALGO_SHA1 = "SHA-1";
	
	/**
	 * <p>获取MD5散列算法对象</p>
	 * 
	 * @return MD5散列算法对象
	 * 
	 * @see {@link #digest(String)}
	 */
	public static final MessageDigest md5() {
		return digest(ALGO_MD5);
	}

	/**
	 * <p>获取SHA-1散列算法对象</p>
	 * 
	 * @return SHA-1散列算法对象
	 * 
	 * @see {@link #digest(String)}
	 */
	public static final MessageDigest sha1() {
		return digest(ALGO_SHA1);
	}
	
	/**
	 * <p>获取散列算法对象</p>
	 * 
	 * @param algo 算法名称
	 * 
	 * @return 算法对象
	 */
	public static final MessageDigest digest(String algo) {
		try {
			return MessageDigest.getInstance(algo);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("不支持的散列算法：{}", algo, e);
		}
		return null;
	}
	
}
