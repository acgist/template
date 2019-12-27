package com.acgist.test;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.PublicKey;

import org.junit.Test;

import com.acgist.core.HTTPClient;
import com.acgist.core.exception.NetException;
import com.acgist.core.gateway.gateway.request.UserRequest;
import com.acgist.core.gateway.gateway.request.UserUpdateRequest;
import com.acgist.core.gateway.gateway.response.UserResponse;
import com.acgist.utils.DateUtils;
import com.acgist.utils.GatewayUtils;
import com.acgist.utils.JSONUtils;
import com.acgist.utils.RsaUtils;

public class UserServiceTest extends BaseTest {
	
	private PublicKey publicKey = RsaUtils.loadPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqiYhQbeAKka23R0aP/szdmHTgjZA3QK5ZZ7Rbx+EN3s43e0TGRtWXg5bkgro8VhP8+DIlwAP6UoCRWTaN/nAVBZNnnvJOXjnPAeiQTIP6Z5enmQ7kjDyXciYo7eObZ/A/ZGEHVg0vJNxcMRAlrThVHx/L7xzeyvdmYh7IJ51piEt8VxjfRgeIoojQB2CBS62xz0Rs+pVdJJYTRFgQ7gfR1P+QKJ8gdVpDmdYljQzymv3ELV7fhoe7snSRIHYzYsn6GiHJk7XoTKd0L4oS8NQtI7RkMb7u2xoK4kOMc3bQ5RsKEUpp1EI4b3l/mrSXGM9EGLEomDeSZUOz/CpwjxCNQIDAQAB");
	
	@Test
	public void testCost() throws NetException, InterruptedException {
		UserUpdateRequest request = new UserUpdateRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setNick("你说什么");
		request.setUsername("testtest");
		GatewayUtils.signature("test", request);
		this.cost(10000, 100, (v) -> {
			try {
				HTTPClient.newInstance("http://localhost:28800/gateway/user/update").post(request.toString(), BodyHandlers.ofString());
			} catch (NetException e) {
				this.log(e);
			}
		});
	}
	
	@Test
	public void testUser() throws NetException {
		UserRequest request = new UserRequest();
		request.setRequestTime(DateUtils.nowTimestamp());
		request.setReserved("测试");
		request.setUsername("testtest");
		GatewayUtils.signature("test", request);
		this.log(request.toString());
		HttpResponse<String> body = HTTPClient.newInstance("http://localhost:28800/gateway/user").post(request.toString(), BodyHandlers.ofString());
		String json = body.body();
		this.log(json);
		final var response = JSONUtils.toJava(json, UserResponse.class);
		this.log("验签：" + GatewayUtils.verify(this.publicKey, response));
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
		String json = body.body();
		this.log(json);
		final var response = JSONUtils.toJava(json, UserResponse.class);
		this.log("验签：" + GatewayUtils.verify(this.publicKey, response));
	}
	
}
