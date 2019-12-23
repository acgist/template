package com.acgist.core.service;

/**
 * <p>service - 编号生成</p>
 */
public interface ICodeService {

	/**
	 * <p>编号类型</p>
	 */
	public enum CodeType {
		
		/** 订单编号 */
		ORDER("O"),
		/** 支付编号 */
		BUSINESS("B");
		
		private final String value;

		private CodeType(String value) {
			this.value = value;
		}

		public String value() {
			return this.value;
		}
		
	}
	
	/**
	 * <p>生成编号</p>
	 * <p>格式：编号类型 + SN + yyyyMMddHHmmss + INDEX</p>
	 * 
	 * @param codeType 编号类型
	 * 
	 * @return 订单编号
	 */
	String buildCode(CodeType codeType);
	
}
