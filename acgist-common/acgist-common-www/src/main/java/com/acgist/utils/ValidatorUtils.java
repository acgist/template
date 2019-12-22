package com.acgist.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.acgist.core.gateway.request.APIRequest;
import com.acgist.core.pojo.message.BaseMessage;

/**
 * utils - 数据校验
 */
public class ValidatorUtils {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
	/**
	 * 请求校验数据：<br>
	 * 	成功返回：null<br>
	 * 	失败返回：错误信息
	 */
	public static final String verify(APIRequest request) {
		if(request == null) {
			return null;
		}
		final StringBuffer messageBuilder = new StringBuffer();
		final Set<ConstraintViolation<APIRequest>> set = VALIDATOR.validate(request, Default.class);
		if (set != null && !set.isEmpty()) {
			for (ConstraintViolation<APIRequest> violation : set) {
				messageBuilder
					.append(violation.getMessage())
					.append("[")
					.append(violation.getPropertyPath().toString())
					.append("]")
					.append("&");
			}
		}
		if(messageBuilder.length() == 0) {
			return null;
		}
		return messageBuilder.substring(0, messageBuilder.length() - 1);
	}
	
	/**
	 * 消息校验数据：<br>
	 * 	成功返回：null<br>
	 * 	失败返回：错误信息
	 */
	public static final String verify(BaseMessage message) {
		if(message == null) {
			return null;
		}
		final StringBuffer messageBuilder = new StringBuffer();
		final Set<ConstraintViolation<BaseMessage>> set = VALIDATOR.validate(message, Default.class);
		if (set != null && !set.isEmpty()) {
			for (ConstraintViolation<BaseMessage> violation : set) {
				messageBuilder
					.append(violation.getMessage())
					.append("[")
					.append(violation.getPropertyPath().toString())
					.append("]")
					.append("&");
			}
		}
		if(messageBuilder.length() == 0) {
			return null;
		}
		return messageBuilder.substring(0, messageBuilder.length() - 1);
	}
	
}
