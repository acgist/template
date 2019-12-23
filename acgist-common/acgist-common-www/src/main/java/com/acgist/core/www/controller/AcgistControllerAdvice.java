package com.acgist.core.www.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.exception.ErrorCodeException;
import com.acgist.utils.RedirectUtils;

/**
 * <p>统一异常处理</p>
 * <p>异常和状态编码参考：DefaultHandlerExceptionResolver</p>
 */
@ControllerAdvice
public class AcgistControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(AcgistControllerAdvice.class);
	
	/**
	 * <p>所有异常处理</p>
	 * 
	 * @param e 异常
	 * @param request 请求
	 * @param response 响应
	 */
	@Primary
	@ExceptionHandler(Exception.class)
	public void exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.error("系统异常", e);
		if (e instanceof ErrorCodeException) {
			final var exception = (ErrorCodeException) e;
			final var code = AcgistCode.valueOfCode(exception.getCode());
			RedirectUtils.error(code, e.getMessage(), request, response);
		} else {
			final var code = valueOfThrowable(e, response);
			RedirectUtils.error(code, request, response);
		}
	}
	
	/**
	 * <p>数据校验异常</p>
	 * 
	 * @param e 异常
	 * @param request 请求
	 * @param response 响应
	 */
	@Primary
	@ExceptionHandler(BindException.class)
	public void bindException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.error("系统异常", e);
		final var exception = (BindException) e;
		final var message = exception.getAllErrors().stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.joining("，"));
		RedirectUtils.error(AcgistCode.CODE_3000, message, request, response);
	}
	
	/**
	 * <p>通过异常获取状态编码</p>
	 * 
	 * @param e 异常
	 * @param response 响应
	 * 
	 * @return 状态编码
	 */
	private AcgistCode valueOfThrowable(Throwable e, HttpServletResponse response) {
		if (e == null) {
			return AcgistCode.CODE_9999;
		}
		Throwable t = e;
		AcgistCode code;
		while (t.getCause() != null) {
			t = t.getCause();
		}
		if (t instanceof HttpRequestMethodNotSupportedException) {
			code = AcgistCode.CODE_4405;
		} else if (t instanceof HttpMediaTypeNotSupportedException) {
			code = AcgistCode.CODE_4415;
		} else if (t instanceof HttpMessageNotReadableException) {
			code = AcgistCode.CODE_4400;
		} else if (response != null) {
			code = AcgistCode.valueOfStatus(response.getStatus());
		} else {
			code = AcgistCode.CODE_9999;
		}
		return code;
	}

}
