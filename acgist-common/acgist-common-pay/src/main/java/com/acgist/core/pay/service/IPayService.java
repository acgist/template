package com.acgist.core.pay.service;

/**
 * <p>服务 - 支付</p>
 */
public interface IPayService {

	/**
	 * <p>创建支付编号</p>
	 * <p>格式："B" + SN + yyyyMMddHHmmss + INDEX</p>
	 * 
	 * @return 支付编号
	 */
	String buildCode();
	
}
