package com.acgist.core.exception;

/**
 * <p>网络异常</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class NetException extends Exception {

	private static final long serialVersionUID = 1L;

	public NetException() {
		super("网络异常");
	}

	public NetException(String message) {
		super(message);
	}

	public NetException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
	
	public NetException(String message, Throwable cause) {
		super(message, cause);
	}

}
