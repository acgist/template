package com.api.core.pojo.message;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.message.BaseMessage;
import com.acgist.core.pojo.message.ResultMessage;

/**
 * message - 服务间通信，返回结果
 */
public class ResultMessage extends BaseMessage {

	private static final long serialVersionUID = 1L;

	protected String code;
	protected String message;

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
	 * 是否成功
	 */
	public boolean isSuccess() {
		return AcgistCode.CODE_SUCCESS.equals(this.getCode());
	}
	
	/**
	 * 是否失败
	 */
	public boolean isFail() {
		return !isSuccess();
	}

	public ResultMessage buildSuccess() {
		return buildMessage(AcgistCode.CODE_0000);
	}

	public ResultMessage buildFail() {
		return buildMessage(AcgistCode.CODE_9999);
	}

	public ResultMessage buildMessage(AcgistCode code) {
		return buildMessage(code.getCode(), code.getMessage());
	}

	public ResultMessage buildMessage(AcgistCode code, String message) {
		return buildMessage(code.getCode(), message);
	}

	public ResultMessage buildMessage(String code, String message) {
		this.code = code;
		this.message = message;
		return this;
	}

}
