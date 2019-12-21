package com.api.core.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.controller.APIErrorController;
import com.acgist.core.gateway.response.APIResponse;

/**
 * 统一错误页面
 */
@Controller
public class APIErrorController implements ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(APIErrorController.class);
	
	public static final String ERROR_PATH = "/error";
	
	/**
	 * JSON错误处理
	 */
	@Primary
	@ResponseBody
	@RequestMapping(value = ERROR_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String index(String code, String message, HttpServletResponse response) {
//		request.getAttribute("javax.servlet.error.message"); // 错误信息
		final AcgistCode apiCode = code(code, response);
		message = AcgistCode.message(apiCode, message);
		LOGGER.warn("系统错误（接口），错误代码：{}，错误描述：{}", apiCode.getCode(), message);
		return APIResponse.builder().buildMessage(apiCode, message).response();
	}

	/**
	 * 非JSON错误处理
	 */
	@Primary
	@RequestMapping(value = ERROR_PATH, produces = MediaType.TEXT_HTML_VALUE)
	public String index(String code, String message, ModelMap model, HttpServletResponse response) {
		final AcgistCode apiCode = code(code, response);
		message = AcgistCode.message(apiCode, message);
		model.put("code", apiCode.getCode());
		model.put("message", message);
		LOGGER.warn("系统错误（页面），错误代码：{}，错误描述：{}", apiCode.getCode(), message);
		return getErrorPath();
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
	
	/**
	 * 错误代码转换，如果错误代码为空，使用相应代码获取错误代码
	 * @param code 错误代码
	 * @param response 响应
	 * @return 错误代码
	 */
	private AcgistCode code(String code, HttpServletResponse response) {
		if(code == null) {
			return AcgistCode.valueOfStatus(response.getStatus());
		}
		return AcgistCode.valueOfCode(code);
	}
	
}
