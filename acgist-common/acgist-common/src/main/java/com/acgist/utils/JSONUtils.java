package com.acgist.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>utils - JSON</p>
 */
public class JSONUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

	/**
	 * <p>将Java对象转JSON字符串</p>
	 * 
	 * @param object Java对象
	 * 
	 * @return JSON字符串
	 */
	public static final String toJSON(Object object) {
		if (object == null) {
			return null;
		}
		final ObjectMapper mapper = new ObjectMapper();
		try {
			// 使用注解：@JsonInclude(Include.NON_NULL)
			mapper.setSerializationInclusion(Include.NON_NULL);
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOGGER.error("Java对象转JSON字符串异常：{}", object, e);
		}
		return null;
	}

	/**
	 * <p>将JSON字符串转Map对象</p>
	 * 
	 * @param json JSON字符串
	 * 
	 * @return Map对象
	 */
	public static final Map<String, Object> toMap(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final JavaType type = mapper.getTypeFactory().constructParametricType(Map.class, String.class, Object.class);
			return mapper.readValue(json, type);
		} catch (IOException e) {
			LOGGER.error("JSON字符串转Map对象异常：{}", json, e);
		}
		return null;
	}
	
	/**
	 * <p>将JSON字符串转Map对象</p>
	 * 
	 * @param json JSON字符串
	 * 
	 * @return Map对象
	 */
	public static final Map<Object, Object> toMapEx(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final JavaType type = mapper.getTypeFactory().constructParametricType(Map.class, Object.class, Object.class);
			return mapper.readValue(json, type);
		} catch (IOException e) {
			LOGGER.error("JSON字符串转Map对象异常：{}", json, e);
		}
		return null;
	}
	
	/**
	 * <p>将JSON字符串转List对象</p>
	 * 
	 * @param <T> 类型
	 * @param json JSON字符串
	 * @param clazz 类型
	 * 
	 * @return List对象
	 */
	public static final <T> List<T> toList(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final JavaType type = mapper.getTypeFactory().constructParametricType(List.class, clazz);
			return mapper.readValue(json, type);
		} catch (IOException e) {
			LOGGER.error("JSON字符串转List对象异常：{}", json, e);
		}
		return null;
	}
	
	/**
	 * <p>将JSON字符串转Java对象</p>
	 * 
	 * @param <T> 类型
	 * @param json JSON字符串
	 * @param clazz 类型
	 * 
	 * @return Java对象
	 */
	public static final <T> T toJava(String json, Class<T> clazz) {
		if(json == null || clazz == null) {
			return null;
		}
		final ObjectMapper mapper = new ObjectMapper();
		try {
			// 使用注解：@JsonIgnoreProperties(ignoreUnknown = true)
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			LOGGER.error("JSON字符串转Java对象异常：{}", json, e);
		}
		return null;
	}

}
