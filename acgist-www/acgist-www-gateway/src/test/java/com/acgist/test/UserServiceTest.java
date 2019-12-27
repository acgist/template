package com.acgist.test;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.Test;

import com.acgist.core.HTTPClient;
import com.acgist.core.exception.NetException;
import com.acgist.core.gateway.gateway.request.UserRequest;
import com.acgist.core.gateway.gateway.request.UserUpdateRequest;
import com.acgist.core.gateway.gateway.response.UserResponse;
import com.acgist.utils.DateUtils;
import com.acgist.utils.GatewayUtils;
import com.acgist.utils.JSONUtils;

public class UserServiceTest extends BaseTest {
	
	@Test
	public void testCost() throws NetException, InterruptedException {
		UserUpdateRequest request = new UserUpdateRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setNick("你说什么");
		request.setUsername("testtest");
		GatewayUtils.signature("test", request);
		this.cost(10000, 100, (a) -> {
			try {
				HTTPClient.newInstance("http://localhost:28800/gateway/user/update").post(request.toString(), BodyHandlers.ofString());
			} catch (NetException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	
	@Test
	public void testUser() throws NetException {
		String password = "test";
		UserRequest request = new UserRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setUsername("testtest");
		GatewayUtils.signature(password, request);
		this.log(request.toString());
		HttpResponse<String> body = HTTPClient.newInstance("http://localhost:28800/gateway/user").post(request.toString(), BodyHandlers.ofString());
		String json = body.body();
		this.log(json);
		final var response = JSONUtils.toJava(json, UserResponse.class);
		this.log("验签：" + GatewayUtils.verify(password, response));
	}
	
	@Test
	public void testUserUpdate() throws NetException {
		UserUpdateRequest request = new UserUpdateRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setNick("你说什么");
		request.setUsername("testtest");
		GatewayUtils.signature("test", request);
		this.log(request.toString());
		HttpResponse<String> body = HTTPClient.newInstance("http://localhost:28800/gateway/user/update").post(request.toString(), BodyHandlers.ofString());
		this.log(body.body());
	}
	
}
