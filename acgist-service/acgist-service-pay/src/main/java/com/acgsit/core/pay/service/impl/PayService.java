package com.acgsit.core.pay.service.impl;

import java.util.Date;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

import com.acgist.core.pay.service.IPayService;
import com.acgist.utils.DateUtils;

@Service(retries = 0, version = "${acgist.service.version}")
public class PayService implements IPayService {

	private static final int MIN_INDEX = 10000;
	private static final int MAX_INDEX = 99999;
	
	/**
	 * <p>支付索引</p>
	 */
	private int index = 10000;
	
	@Value("${acgist.sn:10}")
	private String sn;
	
	@Override
	public String buildCode() {
		final StringBuilder codeBuilder = new StringBuilder("B");
		codeBuilder.append(this.sn);
		codeBuilder.append(DateUtils.format(new Date()));
		codeBuilder.append(this.index);
		synchronized (this) {
			if(this.index++ == MAX_INDEX) {
				this.index = MIN_INDEX;
			}
		}
		return codeBuilder.toString();
	}
	
}
