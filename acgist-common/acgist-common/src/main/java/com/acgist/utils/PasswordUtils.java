package com.acgist.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * <p>utils - 密码</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class PasswordUtils {

	/**
	 * <p>密码加密</p>
	 * <p>直接使用MD5编码</p>
	 * 
	 * @param password 密码
	 * 
	 * @return 加密密码
	 */
	public static final String encrypt(String password) {
		return DigestUtils.md5Hex(password);
	}
	
}
