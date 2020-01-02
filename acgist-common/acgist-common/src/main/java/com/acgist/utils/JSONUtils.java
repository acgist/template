package com.acgist.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

/**
 * <p>utils - JSON</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class JSONUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

	/**
	 * <p>ObjectMapper</p>
	 * <p>线程安全，建议使用单例可以明显提升性能。</p>
	 */
	private static final ObjectMapper MAPPER = buildMapper();
	
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
		final ObjectMapper mapper = getMapper();
		try {
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
	public static final Map<String, String> toMapSimple(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
		try {
			final JavaType type = mapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);
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
	public static final Map<String, Object> toMap(String json) {
		if (json == null) {
			return null;
		}
		final ObjectMapper mapper = getMapper();
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
		final ObjectMapper mapper = getMapper();
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
		final ObjectMapper mapper = getMapper();
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
		final ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			LOGGER.error("JSON字符串转Java对象异常：{}", json, e);
		}
		return null;
	}
	
	/**
	 * <p>获取ObjectMapper</p>
	 * 
	 * @return ObjectMapper
	 */
	public static final ObjectMapper getMapper() {
		return MAPPER;
	}
	
	/**
	 * <p>创建默认ObjectMapper</p>
	 * 
	 * @return ObjectMapper
	 */
	public static final ObjectMapper buildMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
			.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
	
	/**
	 * <p>创建序列化ObjectMapper</p>
	 * 
	 * @return ObjectMapper
	 */
	public static final ObjectMapper buildSerializeMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		final PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
			.allowIfBaseType(Object.class)
			.build();
		mapper
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
			.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL)
			.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}

}
