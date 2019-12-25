package com.acgist.core.service.impl;

import java.util.Date;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

import com.acgist.core.service.ICodeService;
import com.acgist.utils.DateUtils;

/**
 * <p>service - 编号</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class CodeService implements ICodeService {

	/**
	 * <p>编号最小索引</p>
	 */
	private static final int MIN_INDEX = 10000;
	/**
	 * <p>编号最大索引</p>
	 */
	private static final int MAX_INDEX = 99999;
	
	/**
	 * <p>编号索引</p>
	 */
	private int index = MIN_INDEX;
	
	@Value("${acgist.sn:10}")
	private String sn;
	
	@Override
	public String buildCode(CodeType codeType) {
		final StringBuilder codeBuilder = new StringBuilder(codeType.value());
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
