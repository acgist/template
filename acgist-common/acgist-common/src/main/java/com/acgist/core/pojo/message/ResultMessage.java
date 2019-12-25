package com.acgist.core.pojo.message;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.Pojo;

/**
 * <p>message - 结果消息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class ResultMessage extends Pojo {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>编码</p>
	 */
	protected String code;
	/**
	 * <p>消息</p>
	 */
	protected String message;

	public static final ResultMessage newInstance() {
		return new ResultMessage();
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * <p>判断是否成功</p>
	 * 
	 * @return 是否成功
	 */
	public boolean success() {
		return AcgistCode.success(this.code);
	}

	/**
	 * <p>判断是否失败</p>
	 * 
	 * @return 是否失败
	 */
	public boolean fail() {
		return !success();
	}
	
	public ResultMessage buildSuccess() {
		return buildMessage(AcgistCode.CODE_0000);
	}

	public ResultMessage buildFail() {
		return buildMessage(AcgistCode.CODE_9999);
	}

	public ResultMessage buildMessage(AcgistCode code) {
		return buildMessage(code, code.getMessage());
	}
	
	public ResultMessage buildMessage(AcgistCode code, String message) {
		this.code = code.getCode();
		this.message = AcgistCode.message(code, message);
		return this;
	}

}
