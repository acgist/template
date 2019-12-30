package com.acgist.core.controller;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public String publicKey() {
		return RsaUtils.toString(this.publicKey);
	}
	
	/**
	 * <p>获取私钥</p>
	 * 
	 * @return 私钥
	 */
	@GetMapping("/private/key")
	public String privateKey() {
		// 同样返回公钥
		return RsaUtils.toString(this.publicKey);
	}
	
}
