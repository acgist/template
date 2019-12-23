package com.acgist.core.pay.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.acgist.core.pay.service.IPayService;

@Service(retries = 0, version = "${acgist.service.version}")
public class PayService implements IPayService {

}
