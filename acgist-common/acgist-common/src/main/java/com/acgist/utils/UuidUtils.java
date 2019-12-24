package com.acgist.utils;

import java.util.UUID;

/**
 * <p>utils - UUID</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class UuidUtils {

	/**
	 * <p>生成UUID</p>
	 * 
	 * @return UUID
	 */
	public static final String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
}
