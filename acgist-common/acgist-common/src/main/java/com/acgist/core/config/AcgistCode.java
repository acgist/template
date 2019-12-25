package com.acgist.core.config;

import com.acgist.core.exception.ErrorCodeException;

/**
 * <p>config - 状态编码</p>
 * <table border="1">
 * 	<tr>
 * 		<th>状态编码</th><th>描述</th>
 * 	</tr>
 * 	<tr>
 * 		<td>{@code 0000}</td><td>成功</td>
 * 	</tr>
 * 	<tr>
 * 		<td>{@code 9999}</td><td>未知错误</td>
 * 	</tr>
 * 	<tr>
 * 		<td>{@code 1xxx}</td><td>服务错误</td>
 * 	</tr>
 * 	<tr>
 * 		<td>{@code 2xxx}</td><td>业务错误</td>
 * 	</tr>
 * 	<tr>
 * 		<td>{@code 3xxx}</td><td>数据错误</td>
 * 	</tr>
 * 	<tr>
 * 		<td>{@code 4xxx}</td><td>系统错误：原始HTTP状态编码</td>
 * 	</tr>
 * </table>
 * 
 * @author acgist
 * @since 1.0.0
 */
public enum AcgistCode {

	CODE_0000("0000", "成功"),
	
	CODE_1000("1000", "未知服务"),
	CODE_1002("1001", "服务超时"),
	
	CODE_2000("2000", "鉴权失败"),
	CODE_2001("2001", "无此权限"),
	
	CODE_3000("3000", "格式错误"),
	CODE_3001("3001", "验签失败"),
	CODE_3002("3002", "请求超时"),
	
	CODE_4400("4400", "请求无效"),
	CODE_4403("4403", "请求无效"),
	CODE_4404("4404", "资源不存在"),
	CODE_4405("4405", "服务方法不支持"),
	CODE_4415("4415", "服务类型不支持"),
	CODE_4503("4503", "服务不可用"),
	
	CODE_9999("9999", "未知错误");
	
	/**
	 * <p>状态编码</p>
	 */
	private String code;
	/**
	 * <p>状态描述</p>
	 */
	private String message;

	/**
	 * <p>HTTP状态编码前缀：{@value}</p>
	 */
	private static final String HTTP_STATUS_CODE_PREFIX = "4";
	
	/**
	 * <p>成功编码</p>
	 */
	private static final String SUCCESS_CODE = AcgistCode.CODE_0000.getCode();
	
	private AcgistCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}

	/**
	 * <p>通过状态编码获取{@code AcgistCode}</p>
	 * 
	 * @param code 状态编码
	 * 
	 * @return {@code AcgistCode}
	 */
	public static final AcgistCode valueOfCode(String code) {
		for (AcgistCode acgistCode : AcgistCode.values()) {
			if (acgistCode.code.equals(code)) {
				return acgistCode;
			}
		}
		return CODE_9999;
	}
	
	/**
	 * <p>通过HTTP状态编码获取{@code AcgistCode}</p>
	 * 
	 * @param statusCode HTTP状态编码
	 * 
	 * @return {@code AcgistCode}
	 */
	public static final AcgistCode valueOfStatus(int statusCode) {
		return valueOfCode(HTTP_STATUS_CODE_PREFIX + statusCode);
	}
	
	/**
	 * <p>判断是否成功</p>
	 * 
	 * @return 是否成功
	 */
	public static final boolean success(String code) {
		return AcgistCode.SUCCESS_CODE.equals(code);
	}
	
	/**
	 * <p>获取错误信息</p>
	 * 
	 * @param code 错误编码
	 * @param e 异常
	 * 
	 * @return 错误信息
	 */
	public static final String message(AcgistCode code, Throwable e) {
		if(e == null) {
			return code.getMessage();
		}
		String message = null;
		if (e instanceof ErrorCodeException) {
			message = e.getMessage();
		}
		return message(code, message);
	}
	
	/**
	 * <p>获取错误信息</p>
	 * 
	 * @param code 错误编码
	 * @param message 错误信息
	 * 
	 * @return 错误信息
	 */
	public static final String message(AcgistCode code, String message) {
		if(message == null || message.isEmpty()) {
			message = code.getMessage();
		}
		return message;
	}
	
}
