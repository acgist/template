package com.acgist.core.gateway.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.message.ResultMessage;

/**
 * <p>统一错误页面</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Controller
public class AcgistErrorController implements ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AcgistErrorController.class);
	
	/**
	 * <p>错误请求地址</p>
	 */
	public static final String ERROR_PATH = "/error";
	
	/**
	 * <p>处理JSON错误</p>
	 * 
	 * @param code 错误编码
	 * @param message 错误信息
	 * @param response 响应
	 */
	@Primary
	@ResponseBody
	@RequestMapping(value = ERROR_PATH)
	public ResultMessage index(String code, String message, HttpServletResponse response) {
		final AcgistCode acgistCode = AcgistCode.valueOfCode(code);
		message = AcgistCode.message(acgistCode, message);
		LOGGER.warn("系统错误（接口），错误编码：{}，错误描述：{}", acgistCode.getCode(), message);
		return ResultMessage.newInstance().buildMessage(acgistCode, message);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}
