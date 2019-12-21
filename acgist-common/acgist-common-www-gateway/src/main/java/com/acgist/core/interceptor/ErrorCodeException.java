package com.acgist.core.interceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.config.HttpMediaTypeNotSupportedException;
import com.acgist.core.config.HttpMessageNotReadableException;
import com.acgist.core.config.HttpRequestMethodNotSupportedException;
import com.acgist.core.config.HttpServletResponse;
import com.acgist.core.exception.ErrorCodeException;

public class ErrorCodeException {
	/**
	 * 通过异常信息获取APICode
	 * @param e 异常信息
	 * @param response 响应
	 */
	public static final AcgistCode valueOfThrowable(final Throwable e, HttpServletResponse response) {
		if(e == null) {
			return AcgistCode.CODE_9999;
		}
		Throwable t = e;
		while(t.getCause() != null) {
			t = t.getCause();
		}
		AcgistCode code;
		if (t instanceof ErrorCodeException) {
			ErrorCodeException exception = (ErrorCodeException) t;
			code = AcgistCode.valueOfCode(exception.getErrorCode());
		} else if (t instanceof HttpRequestMethodNotSupportedException) {
			code = AcgistCode.CODE_4405;
		} else if (t instanceof HttpMediaTypeNotSupportedException) {
			code = AcgistCode.CODE_4415;
		} else if (t instanceof HttpMessageNotReadableException) {
			code = AcgistCode.CODE_4400;
		} else if(response != null) {
			code = AcgistCode.valueOfStatus(response.getStatus());
		} else {
			code = AcgistCode.CODE_9999;
		}
		return code;
	}
}
