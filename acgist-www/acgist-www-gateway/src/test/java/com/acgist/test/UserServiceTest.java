package com.acgist.test;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.Test;

import com.acgist.core.HTTPClient;
import com.acgist.core.exception.NetException;
import com.acgist.core.gateway.request.UserRequest;
import com.acgist.utils.DateUtils;
import com.acgist.utils.GatewayUtils;

public class UserServiceTest {

	@Test
	public void testUser() throws NetException {
		UserRequest request = new UserRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setUsername("testtest");
		GatewayUtils.sign("test", request);
		System.out.println(request.toString());
		HttpResponse<String> body = HTTPClient.newInstance("http://localhost:28800/gateway/user").post(request.toString(), BodyHandlers.ofString());
		System.out.println(body.body());
	}
	
}
