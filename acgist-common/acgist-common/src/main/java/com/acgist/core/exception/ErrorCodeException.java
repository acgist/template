package com.acgist.core.exception;

import com.acgist.core.config.AcgistCode;

/**
 * <p>异常 - 错误代码</p>
 */
public class ErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ErrorCodeException() {
	}

	public ErrorCodeException(AcgistCode code) {
		this(code.getCode(), code.getMessage());
	}
	
	public ErrorCodeException(AcgistCode code, String message) {
		this(code.getCode(), message);
	}
	
	public ErrorCodeException(String code, String message) {
		super(message);
		this.errorCode = code;
	}

	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
