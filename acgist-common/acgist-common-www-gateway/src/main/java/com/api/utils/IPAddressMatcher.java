package com.api.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * utils - IP匹配：<br>
 * IPv6：0:0::/112<br>
 * IPv4：192.168.1.0/24
 */
public class IPAddressMatcher {

	private final int mask;
	private final InetAddress requestAddress;

	/**
	 * 构造函数
	 * @param address IP地址格式
	 */
	public IPAddressMatcher(String address) {
		if (address.indexOf('/') > 0) {
			String[] addressAndMask = StringUtils.split(address, "/");
			address = addressAndMask[0];
			mask = Integer.parseInt(addressAndMask[1]);
		} else {
			mask = -1;
		}
		this.requestAddress = parseAddress(address);
	}

	/**
	 * 请求验证
	 * @param request 请求
	 * @return 验证结果：true-验证成功、false-验证失败
	 */
	public boolean matches(HttpServletRequest request) {
		return matches(request.getRemoteAddr());
	}

	/**
	 * 请求地址验证
	 * @param address 请求地址
	 * @return 验证结果：true-验证成功、false-验证失败
	 */
	public boolean matches(String address) {
		InetAddress remoteAddress = parseAddress(address);
		if (mask < 0) {
			return remoteAddress.equals(requestAddress);
		}
		byte[] remoteBytes = remoteAddress.getAddress();
		byte[] requestBytes = requestAddress.getAddress();
		int oddBits = mask % 8;
		int maskBytes = mask / 8 + (oddBits == 0 ? 0 : 1);
		byte[] mask = new byte[maskBytes];
		Arrays.fill(mask, 0, oddBits == 0 ? mask.length : mask.length - 1, (byte) 0xFF);
		if (oddBits != 0) {
			int finalByte = (1 << oddBits) - 1;
			finalByte <<= 8 - oddBits;
			mask[mask.length - 1] = (byte) finalByte;
		}
		for (int i = 0; i < mask.length; i++) {
			if ((remoteBytes[i] & mask[i]) != (requestBytes[i] & mask[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 地址转换
	 * @param address 请求地址
	 * @return 地址
	 */
	private InetAddress parseAddress(String address) {
		try {
			return InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("IP地址错误" + address, e);
		}
	}

}
