package com.acgist.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.config.AcgistConst;
import com.acgist.core.exception.ErrorCodeException;
import com.acgist.core.gateway.Gateway;

/**
 * <p>utils - 网关信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class GatewayUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayUtils.class);

	/**
	 * <p>忽略属性：{@value}</p>
	 */
	private static final String CLASS_KEY = AcgistConst.PROPERTY_CLASS;

	/**
	 * <p>网关实例转为网关信息Map</p>
	 * 
	 * @param <T> 网关类型
	 * @param t 网关实例
	 * 
	 * @return 网关信息Map
	 */
	public static final <T extends Gateway> Map<String, String> unpack(T t) {
		if(t == null) {
			return null;
		}
		Map<String, String> data = null;
		try {
			data = BeanUtils.describe(t);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("网关信息转为Map异常：{}", t, e);
		}
		if (data != null) {
			// 过滤
			return data.entrySet().stream()
				.filter(entry -> !CLASS_KEY.equals(entry.getKey())) // class
				.filter(entry -> entry.getKey() != null && entry.getValue() != null) // 空值
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		}
		return null;
	}

	/**
	 * <p>网关属性填充</p>
	 * 
	 * @param gateway 网关实例
	 * @param data 网关信息
	 */
	public static final void pack(Gateway gateway, Map<String, String> data) {
		if(gateway == null || data == null) {
			return;
		}
		try {
			BeanUtils.populate(gateway, data);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("网关属性填充异常：{}", data, e);
		}
	}
	
	/**
	 * <p>签名</p>
	 * 
	 * @param password 密码
	 * @param gateway 网关信息
	 * 
	 * @return 签名
	 */
	public static final String signature(String password, Gateway gateway) {
		if(StringUtils.isEmpty(password) || gateway == null) {
			throw new ErrorCodeException(AcgistCode.CODE_3000);
		}
		final Map<String, String> data = gateway.data();
		final StringBuffer buffer = new StringBuffer(password);
		data.entrySet().stream()
			.filter(entry -> !Gateway.PROPERTY_SIGNATURE.equals(entry.getKey()))
			.sorted((a, b) -> StringUtils.compare(a.getKey(), b.getKey()))
			.forEach(entry -> {
				buffer.append(entry.getKey()).append(entry.getValue());
			});
		buffer.append(password);
		gateway.setSignature(DigestUtils.md5Hex(buffer.toString()));
		return gateway.getSignature();
	}
	
	/**
	 * <p>验签</p>
	 * 
	 * @param password 密码
	 * @param gateway 网关信息
	 * 
	 * @return 验证结果
	 */
	public static final boolean verify(String password, Gateway gateway) {
		if(StringUtils.isEmpty(password) || gateway == null) {
			throw new ErrorCodeException(AcgistCode.CODE_3000);
		}
		final Map<String, String> data = gateway.data();
		final String signature = data.get(Gateway.PROPERTY_SIGNATURE);
		final String trueSignature = signature(password, gateway);
		return StringUtils.equals(signature, trueSignature);
	}

}
