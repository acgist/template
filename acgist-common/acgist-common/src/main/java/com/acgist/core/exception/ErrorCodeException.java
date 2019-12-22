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
		this.code = code;
	}

	/**
	 * <p>错误编码</p>
	 * 
	 * @see {@link AcgistCode}
	 */
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
