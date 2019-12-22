package com.acgist.core.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.acgist.core.config.AcgistConst;

/**
 * service - 唯一编号生成
 */
@Service
public class UniqueNumberService {

	@Value("${server.sn:10}")
	private String serverSN;

	private static final int MIN_INDEX = 1000;
	private static final int MAX_INDEX = 9999;
	private static int ID_INDEX = MIN_INDEX;
	
	/**
	 * 系统唯一编号，生成ID长度：20<br>
	 * 格式：ServerSN(2) + DATE(14) + INDEX(4)
	 */
	public synchronized String buildId() {
		final StringBuffer builder = new StringBuffer(this.serverSN);
		final SimpleDateFormat formater = new SimpleDateFormat(AcgistConst.TIMESTAMP_FORMAT_LIMIT);
		builder.append(formater.format(new Date()));
		builder.append(ID_INDEX);
		ID_INDEX++;
		if(ID_INDEX > MAX_INDEX) {
			ID_INDEX = MIN_INDEX;
		}
		return builder.toString();
	}
	
}
