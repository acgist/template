package com.acgist.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.core.config.AcgistConst;

/**
 * <p>utils - 日期时间</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class DateUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
	
	/**
	 * <p>获取当前时间戳</p>
	 * 
	 * @return 当前时间戳
	 */
	public static final String nowTimestamp() {
		return format(new Date());
	}
	
	/**
	 * <p>日期格式化</p>
	 * 
	 * @param date 日期
	 * 
	 * @return 日期字符串
	 */
	public static final String format(Date date) {
		return format(date, AcgistConst.TIMESTAMP_FORMAT);
	}
	
	/**
	 * <p>日期格式化</p>
	 * 
	 * @param date 日期
	 * @param format 格式
	 * 
	 * @return 日期字符串
	 */
	public static final String format(Date date, String format) {
		if(date == null) {
			return null;
		}
		final SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(date);
	}
	
	/**
	 * <p>日期转换</p>
	 * 
	 * @param time 日期字符串
	 * 
	 * @return 日期
	 */
	public static final Date parse(String time) {
		return parse(time, AcgistConst.TIMESTAMP_FORMAT);
	}
	
	/**
	 * <p>日期转换</p>
	 * 
	 * @param time 日期字符串
	 * @param format 格式
	 * 
	 * @return 日期
	 */
	public static final Date parse(String time, String format) {
		if(time == null) {
			return null;
		}
		final SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			return formater.parse(time);
		} catch (ParseException e) {
			LOGGER.error("日期转换异常：{}", time, e);
		}
		return null;
	}
	
}
