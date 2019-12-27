package com.acgist.test;

import org.junit.Test;

import com.acgist.utils.RsaUtils;

public class RsaUtilsTest extends BaseTest {

	@Test
	public void testSignature() throws Exception {
		final var keys = RsaUtils.buildKey();
		final var publicKey = keys.get(RsaUtils.PUBLIC_KEY);
		final var privateKey = keys.get(RsaUtils.PRIVATE_KEY);
		this.log(publicKey);
		this.log(privateKey);
		String data = "1234";
		String signature = RsaUtils.signature(data, RsaUtils.loadPrivateKey(privateKey));
		this.log(signature);
		boolean result = RsaUtils.verify(data, signature, RsaUtils.loadPublicKey(publicKey));
		this.log(result);
	}
	
	@Test
	public void testEncrypt() throws Exception {
		final var keys = RsaUtils.buildKey();
		final var publicKey = keys.get(RsaUtils.PUBLIC_KEY);
		final var privateKey = keys.get(RsaUtils.PRIVATE_KEY);
		String data = "1234";
		final var pubKey = RsaUtils.loadPublicKey(publicKey);
		final var priKey = RsaUtils.loadPrivateKey(privateKey);
//		this.cost(10000, 1, (v) -> {
//			String encrypt = RsaUtils.encrypt(pubKey, data);
//			RsaUtils.decrypt(priKey, encrypt);
//		});
		String encrypt = RsaUtils.encrypt(pubKey, data);
		this.log(encrypt);
		String decrypt = RsaUtils.decrypt(priKey, encrypt);
		this.log(decrypt);
	}
	
}
