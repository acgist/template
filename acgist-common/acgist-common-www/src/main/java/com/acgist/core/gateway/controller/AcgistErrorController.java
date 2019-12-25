package com.acgist.core.gateway.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.data.service.pojo.message.AuthoMessage;
import com.acgist.utils.GatewayUtils;

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
	
	@Autowired
	private ApplicationContext context;
	
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
	public GatewayResponse index(String code, String message, HttpServletResponse response) {
		AcgistCode acgistCode;
		if(StringUtils.isEmpty(code)) {
			acgistCode = AcgistCode.valueOfStatus(response.getStatus());
		} else {
			acgistCode = AcgistCode.valueOfCode(code);
		}
		message = AcgistCode.message(acgistCode, message);
		LOGGER.warn("系统错误（接口），错误编码：{}，错误描述：{}", acgistCode.getCode(), message);
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayResponse gatewayResponse = gatewaySession.getResponse();
		final AuthoMessage authoMessage = gatewaySession.getAuthoMessage();
		if(gatewayResponse != null && authoMessage != null) {
			gatewayResponse.setCode(acgistCode.getCode());
			gatewayResponse.setMessage(message);
			GatewayUtils.response(authoMessage.getPassword(), code, message, gatewayResponse);
			return gatewayResponse;
		} else {
			return GatewayResponse.newInstance().buildResponse(acgistCode, message);
		}
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}
