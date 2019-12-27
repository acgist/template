package com.acgist.core.config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.utils.RsaUtils;

/**
 * <p>config - RSA密钥</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
public class RsaConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RsaConfig.class);
	
	/**
	 * <p>公钥</p>
	 */
	@Value("${acgist.public.key:}")
	private String publicKey;
	/**
	 * <p>私钥</p>
	 */
	@Value("${acgist.private.key:}")
	private String privateKey;
	
	@Bean
	public PublicKey publicKey() {
		if(
			StringUtils.isNotEmpty(this.publicKey)
		) {
			LOGGER.info("配置RSA公钥：{}", this.publicKey);
			try {
				// TODO：加载失败加载密钥文件
				return RsaUtils.loadPublicKey(Files.readString(Paths.get(this.getClass().getResource(this.publicKey).toURI())));
			} catch (IOException | URISyntaxException e) {
				LOGGER.error("配置RSA异常", e);
			}
		}
		return null;
	}
	
	@Bean
	public PrivateKey privateKey() {
		if(
			StringUtils.isNotEmpty(this.privateKey)
		) {
			LOGGER.info("配置RSA私钥：{}", this.privateKey);
			try {
				// TODO：加载失败加载密钥文件
				return RsaUtils.loadPrivateKey(Files.readString(Paths.get(this.getClass().getResource(this.privateKey).toURI())));
			} catch (IOException | URISyntaxException e) {
				LOGGER.error("配置RSA异常", e);
			}
		}
		return null;
	}
	
}
