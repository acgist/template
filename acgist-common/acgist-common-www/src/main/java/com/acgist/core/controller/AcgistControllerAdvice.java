package com.acgist.core.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.exception.ErrorCodeException;
import com.acgist.utils.RedirectUtils;

/**
 * <p>统一异常处理</p>
 */
@ControllerAdvice
public class AcgistControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(AcgistControllerAdvice.class);
	
	/**
	 * 异常处理，异常和状态码对应参考：DefaultHandlerExceptionResolver
	 */
	@Primary
	@ExceptionHandler(Exception.class)
	public void exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.error("系统异常", e);
		AcgistCode code = AcgistCode.valueOfThrowable(e, response);
		if (e instanceof ErrorCodeException) {
			RedirectUtils.error(code, e.getMessage(), request, response);
		} else {
			RedirectUtils.error(code, request, response);
		}
	}
	
	/**
	 * 数据校验异常
	 */
	@Primary
	@ExceptionHandler(BindException.class)
	public void bindException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.error("系统异常", e);
		BindException exception = (BindException) e;
		final String message = exception.getAllErrors().stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.joining("，"));
		RedirectUtils.error(AcgistCode.CODE_3000, message, request, response);
	}

}
