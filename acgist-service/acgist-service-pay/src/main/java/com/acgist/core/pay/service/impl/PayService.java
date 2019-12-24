package com.acgist.core.pay.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.acgist.core.service.IPayService;

/**
 * <p>服务 - 支付</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class PayService implements IPayService {

}
