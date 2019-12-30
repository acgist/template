package com.acgist.core.config;

/**
 * <p>config - 全局静态变量</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface AcgistConst {
	
	/**
	 * <p>默认编码：{@value}</p>
	 */
	String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * <p>Class属性：{@value}</p>
	 */
	String PROPERTY_CLASS = "class";

	/**
	 * <p>默认日期格式：{@value}</p>
	 */
	String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * <p>默认时间格式：{@value}</p>
	 */
	String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * <p>默认时间戳格式：{@value}</p>
	 */
	String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
	
	/**
	 * <p>时间戳格式：{@value}</p>
	 */
	String TIMESTAMP_REGEX = "\\d{14}";
	
	/**
	 * <p>邮箱格式：{@value}</p>
	 */
	String MAIL_REGEX = "^[a-zA-Z0-9]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

}
