package com.acgist.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import com.acgist.core.config.AcgistCode;
import com.acgist.utils.URLUtils;

/**
 * <p>utils - 重定向</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class RedirectUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedirectUtils.class);

	/**
	 * <p>错误页面链接</p>
	 */
	private static final String ERROR_LOCATION = "/error?code=%s&message=%s";
	
	/**
	 * <p>跳转错误提示页面</p>
	 * 
	 * @param code 错误编码
	 * @param request 请求
	 * @param response 响应
	 */
	public static final void error(String code, HttpServletRequest request, HttpServletResponse response) {
		final AcgistCode acgistCode = AcgistCode.valueOfCode(code);
		error(acgistCode, acgistCode.getMessage(), request, response);
	}
	
	/**
	 * <p>跳转错误提示页面</p>
	 * 
	 * @param code 错误编码
	 * @param request 请求
	 * @param response 响应
	 */
	public static final void error(AcgistCode code, HttpServletRequest request, HttpServletResponse response) {
		error(code, code.getMessage(), request, response);
	}

	/**
	 * <p>跳转错误提示页面</p>
	 * 
	 * @param code 错误编码
	 * @param message 错误信息
	 * @param request 请求
	 * @param response 响应
	 */
	public static final void error(AcgistCode code, String message, HttpServletRequest request, HttpServletResponse response) {
		message = AcgistCode.message(code, message);
		final String location = String.format(ERROR_LOCATION, code.getCode(), URLUtils.encoding(message));
		requestDispatcher(request, response, location);
	}

	/**
	 * <p>请求转发</p>
	 * 
	 * @param request 请求
	 * @param response 响应
	 * @param location 转发地址
	 */
	public static final void requestDispatcher(HttpServletRequest request, HttpServletResponse response, String location) {
		try {
			request.getRequestDispatcher(location).forward(request, response);
		} catch (ServletException e) {
			LOGGER.error("请求转发异常：{}", location, e);
		} catch (IOException e) {
			LOGGER.error("请求转发异常：{}", location, e);
		}
	}

	/**
	 * <p>303重定向</p>
	 * 
	 * @param response 响应
	 * @param location 转发地址
	 */
	public static final ModelAndView redirectToGet(HttpServletResponse response, String location) {
		if (response != null) {
			response.setStatus(HttpStatus.SEE_OTHER.value());
			response.setHeader("Location", location);
			response.setHeader("Connection", "close");
		}
		return null;
	}

	/**
	 * <p>307重定向</p>
	 * 
	 * @param response 响应
	 * @param location 转发地址
	 */
	public static final void redirectToPost(HttpServletResponse response, String location) {
		if (response != null) {
			response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
			response.setHeader("Location", location);
			response.setHeader("Connection", "close");
		}
	}

}
