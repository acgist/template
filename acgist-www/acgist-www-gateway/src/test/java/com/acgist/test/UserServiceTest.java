package com.acgist.test;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.Test;

import com.acgist.core.HTTPClient;
import com.acgist.core.exception.NetException;
import com.acgist.core.gateway.gateway.request.UserRequest;
import com.acgist.core.gateway.gateway.request.UserUpdateRequest;
import com.acgist.utils.DateUtils;
import com.acgist.utils.GatewayUtils;

public class UserServiceTest extends BaseTest {
	
	@Test
	public void testCost() throws NetException, InterruptedException {
		UserRequest request = new UserRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setUsername("testtest");
		GatewayUtils.sign("test", request);
		this.log(request.toString());
		this.cost(10000, 100, (a) -> {
			try {
				HTTPClient.newInstance("http://localhost:28800/gateway/user").post(request.toString(), BodyHandlers.ofString());
			} catch (NetException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	@Test
	public void testUser() throws NetException {
		UserRequest request = new UserRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setUsername("testtest");
		GatewayUtils.sign("test", request);
		this.log(request.toString());
		HttpResponse<String> body = HTTPClient.newInstance("http://localhost:28800/gateway/user").post(request.toString(), BodyHandlers.ofString());
		this.log(body.body());
	}
	
	@Test
	public void testUserUpdate() throws NetException {
		UserUpdateRequest request = new UserUpdateRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setNick("你说什么");
		request.setUsername("testtest");
		GatewayUtils.sign("test", request);
		this.log(request.toString());
		HttpResponse<String> body = HTTPClient.newInstance("http://localhost:28800/gateway/user/update").post(request.toString(), BodyHandlers.ofString());
		this.log(body.body());
	}
	
}
