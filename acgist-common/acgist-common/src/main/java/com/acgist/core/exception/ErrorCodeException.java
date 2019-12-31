package com.acgist.core.exception;

import com.acgist.core.config.AcgistCode;

/**
 * <p>异常 - 错误代码异常</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class ErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ErrorCodeException() {
		this(AcgistCode.CODE_9999);
	}
	
	public ErrorCodeException(String message) {
		this(AcgistCode.CODE_9999, message);
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
	
	public ErrorCodeException(Throwable t) {
		this(AcgistCode.CODE_9999, t);
	}
	
	public ErrorCodeException(AcgistCode code, Throwable t) {
		this(code.getCode(), code.getMessage(), t);
	}
	
	public ErrorCodeException(AcgistCode code, String message, Throwable t) {
		this(code.getCode(), message, t);
	}
	
	public ErrorCodeException(String code, String message, Throwable t) {
		super(message, t);
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
