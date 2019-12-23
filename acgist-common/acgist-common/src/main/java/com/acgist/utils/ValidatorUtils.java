package com.acgist.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.acgist.core.pojo.Pojo;

/**
 * <p>utils - 数据校验</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class ValidatorUtils {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
	/**
	 * <p>校验数据</p>
	 * 
	 * @param pojo 数据
	 * 
	 * @return 校验结果：{@code null}=成功
	 */
	public static final String verify(Pojo pojo) {
		if(pojo == null) {
			return null;
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
			return null;
		}
		return messageBuilder.substring(0, messageBuilder.length() - 1);
	}
	
}
