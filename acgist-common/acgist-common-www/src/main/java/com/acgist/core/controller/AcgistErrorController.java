package com.acgist.core.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.GatewaySession;
import com.acgist.core.pojo.Pojo;
import com.acgist.core.pojo.message.ResultMessage;

/**
 * <p>统一错误处理</p>
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
	
	@Value("${acgist.error.template:/error}")
	private String error;
	
	@Autowired
	private ApplicationContext context;
	
	/**
	 * <p>统一错误处理</p>
	 * 
	 * @param code 错误编码
	 * @param message 错误信息
	 * @param response 响应
	 */
	@Primary
	@ResponseBody
	@RequestMapping(value = ERROR_PATH)
	public Pojo index(String code, String message, HttpServletResponse response) {
		final AcgistCode acgistCode = acgistCode(code, response);
		message = AcgistCode.message(acgistCode, message);
		LOGGER.warn("系统错误（网关），错误编码：{}，错误描述：{}", acgistCode.getCode(), message);
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		if(gatewaySession.gateway()) {
			return gatewaySession.buildResponse(acgistCode.getCode(), message);
		} else {
			return ResultMessage.newInstance().buildMessage(acgistCode, message);
		}
	}

	/**
	 * <p>页面错误处理</p>
	 * 
	 * @param code 错误编码
	 * @param message 错误信息
	 * @param model Model
	 * @param response 响应
	 */
	@Primary
	@RequestMapping(value = ERROR_PATH, produces = MediaType.TEXT_HTML_VALUE)
	public String index(String code, String message, ModelMap model, HttpServletResponse response) {
		final AcgistCode acgistCode = acgistCode(code, response);
		message = AcgistCode.message(acgistCode, message);
		LOGGER.warn("系统错误（页面），错误编码：{}，错误信息：{}", acgistCode.getCode(), message);
		model.put("code", acgistCode.getCode());
		model.put("message", message);
		return getErrorPath();
	}

	/**
	 * <p>获取状态编码</p>
	 * 
	 * @param code 错误编码
	 * @param response 响应
	 * 
	 * @return 状态编码
	 */
	private AcgistCode acgistCode(String code, HttpServletResponse response) {
		if(StringUtils.isEmpty(code)) {
			return AcgistCode.valueOfStatus(response.getStatus());
		} else {
			return AcgistCode.valueOfCode(code);
		}
	}
	
	@Override
	public String getErrorPath() {
		return this.error;
	}
	
}
