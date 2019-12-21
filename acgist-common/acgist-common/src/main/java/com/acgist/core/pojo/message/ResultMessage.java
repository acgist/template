package com.acgist.core.pojo.message;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.Pojo;

/**
 * <p>message - 服务消息（状态）</p>
 */
public class ResultMessage extends Pojo {

	private static final long serialVersionUID = 1L;

	protected AcgistCode code;

	public AcgistCode getCode() {
		return code;
	}

	public void setCode(AcgistCode code) {
		this.code = code;
	}

	/**
	 * <p>判断是否成功</p>
	 * 
	 * @return 是否成功
	 */
	public boolean success() {
		return this.code != null && AcgistCode.success(this.code.getCode());
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
		this.code = code;
		return this;
	}

}
