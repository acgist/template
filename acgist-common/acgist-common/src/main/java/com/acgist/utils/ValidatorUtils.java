package com.acgist.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.Pojo;
import com.acgist.core.pojo.message.ResultMessage;

/**
 * <p>utils - 数据校验</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class ValidatorUtils {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
	/**
	 * <p>校验数据</p>
	 * 
	 * @param pojo 数据
	 * 
	 * @return 结果
	 */
	public static final ResultMessage verify(Pojo pojo) {
		final ResultMessage message = new ResultMessage();
		if(pojo == null) {
			return message.buildMessage(AcgistCode.CODE_3000);
		}
		final StringBuffer messageBuilder = new StringBuffer();
		final Set<ConstraintViolation<Pojo>> set = VALIDATOR.validate(pojo, Default.class);
		if (set != null && !set.isEmpty()) {
			for (ConstraintViolation<Pojo> violation : set) {
				messageBuilder
					.append(violation.getMessage())
					.append("[")
					.append(violation.getPropertyPath().toString())
					.append("]")
					.append("&");
			}
		}
		if(messageBuilder.length() == 0) {
			return message.buildSuccess();
		}
		messageBuilder.setLength(messageBuilder.length() - 1);
		return message.buildMessage(AcgistCode.CODE_3000, messageBuilder.toString());
	}
	
}
