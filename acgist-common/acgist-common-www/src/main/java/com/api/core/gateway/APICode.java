package com.api.core.gateway;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.exception.ErrorCodeException;

/**
 * 错误状态码<br>
 * 0000=成功<br>
 * 9999=未知错误<br>
 * 1xxx=系统错误<br>
 * 2xxx=业务错误<br>
 * 3xxx=数据错误<br>
 * 4xxx=系统异常（主要处理服务器错误）
 */
public enum APICode {

	CODE_0000("0000", "成功"),
	
	CODE_1000("1000", "未知服务接口"),
	CODE_1002("1002", "服务超时"), // 需要查询
	
	CODE_2000("2000", "用户名和用户密码不匹配"),
	CODE_2001("2001", "无此权限"),
	CODE_2002("2002", "订单不存在"),
	
	CODE_3000("3000", "数据格式错误"),
	CODE_3001("3001", "验签失败"),
	
	CODE_4400("4400", "请求无效"),
	CODE_4403("4403", "请求无效（TOKEN）"),
	CODE_4404("4404", "资源不存在"),
	CODE_4405("4405", "服务不支持（METHOD）"),
	CODE_4415("4415", "服务不支持（MEDIA TYPE）"),
	CODE_4503("4503", "服务不可用"),
	
	CODE_9999("9999", "未知错误");
	
	private String code;
	private String message;

	private static final String RESPONSE_ERROR = "4";
	public static final String CODE_SUCCESS = APICode.CODE_0000.getCode();
	
	private APICode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 通过错误编码获取APICode，如果不存在默认返回9999
	 * @param code 错误编码
	 */
	public static final APICode valueOfCode(String code) {
		for (APICode apiCode : APICode.values()) {
			if(apiCode.code.equals(code)) {
				return apiCode;
			}
		}
		return CODE_9999;
	}
	
	/**
	 * 通过HTTP状态码获取APICode
	 * @param statusCode HTTP状态码
	 */
	public static final APICode valueOfStatus(int statusCode) {
		return valueOfCode(RESPONSE_ERROR + statusCode);
	}
	
	/**
	 * 通过异常信息获取APICode
	 * @param e 异常信息
	 * @param response 响应
	 */
	public static final APICode valueOfThrowable(final Throwable e, HttpServletResponse response) {
		if(e == null) {
			return APICode.CODE_9999;
		}
		Throwable t = e;
		while(t.getCause() != null) {
			t = t.getCause();
		}
		APICode code;
		if (t instanceof ErrorCodeException) {
			ErrorCodeException exception = (ErrorCodeException) t;
			code = APICode.valueOfCode(exception.getErrorCode());
		} else if (t instanceof HttpRequestMethodNotSupportedException) {
			code = APICode.CODE_4405;
		} else if (t instanceof HttpMediaTypeNotSupportedException) {
			code = APICode.CODE_4415;
		} else if (t instanceof HttpMessageNotReadableException) {
			code = APICode.CODE_4400;
		} else if(response != null) {
			code = APICode.valueOfStatus(response.getStatus());
		} else {
			code = APICode.CODE_9999;
		}
		return code;
	}
	
	/**
	 * 通过错误代码和异常获取错误描述
	 * @param code 错误代码
	 * @param e 异常
	 * @return 错误描述
	 */
	public static final String message(APICode code, Throwable e) {
		if(e == null) {
			return code.getMessage();
		}
		String message = null;
		if (e instanceof ErrorCodeException) {
			message = e.getMessage();
		}
		return message(code, message);
	}
	
	/**
	 * 通过错误代码和默认错误描述获取错误描述，如果默认错误描述为空使用错误代码的错误描述
	 * @param code 错误代码
	 * @param message 默认错误描述
	 * @return 错误描述
	 */
	public static final String message(APICode code, String message) {
		if(message == null || message.isEmpty()) {
			message = code.getMessage();
		}
		return message;
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	/**
	 * 判断是否成功
	 */
	public static final boolean success(String code) {
		return APICode.CODE_SUCCESS.equals(code);
	}
	
}
