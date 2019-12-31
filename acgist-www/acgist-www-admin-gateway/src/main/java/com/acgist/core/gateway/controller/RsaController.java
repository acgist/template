package com.acgist.core.gateway.controller;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.utils.RsaUtils;

/**
 * <p>控制器 - RSA</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@RestController
@RequestMapping("/rsa")
public class RsaController {

	@Autowired
	private PublicKey publicKey;
	
	/**
	 * <p>获取公钥</p>
	 * 
	 * @return 公钥
	 */
	@GetMapping("/public/key")
	public ResultMessage publicKey() {
		final var message = ResultMessage.newInstance().buildSuccess();
		message.setMessage(RsaUtils.toString(this.publicKey));
		return message;
	}
	
	/**
	 * <p>获取私钥</p>
	 * 
	 * @return 私钥
	 */
	@GetMapping("/private/key")
	public ResultMessage privateKey() {
		// 同样返回公钥
		final var message = ResultMessage.newInstance().buildSuccess();
		message.setMessage(RsaUtils.toString(this.publicKey));
		return message;
	}
	
}
