package com.api.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.Cipher;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.utils.CAUtils;
import com.acgist.utils.Organ;

/**
 * utils - 证书<br>
 * Cipher不是一个线程安全的类<br>
 * getInstance("RSA"); = getInstance("RSA/ECB/PKCS1Padding");
 */
public class CAUtils {
	
	private static final int KEY_SIZE = 1024;
	private static final int ENCRYPT_LENGTH = 64;
	private static final int DECRYPT_LENGTH = 128;
	private static final String CER_TYPE = "X.509";
	private static final String PFX_TYPE = "PKCS12";
	private static final String RSA_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";

	private static final Logger LOGGER = LoggerFactory.getLogger(CAUtils.class);
	
	/**
	 * 证书创建
	 */
	public static final void create(
			Organ user, Organ creater, String alias, Long validity, String password,
			String cerPath, String pfxPath
		) {
		create(user, creater, alias, validity, password, cerPath, pfxPath, null, null, null);
	}
	
	/**
	 * 创建证书，如果修改请传入原始证书信息
	 * @param issuer 使用者
	 * @param subject 颁发者
	 * @param alias 别名
	 * @param validity 有效期，单位毫秒
	 * @param password 密码
	 * @param cerPath 公钥证书生成
	 * @param pfxPath 私钥证书生成
	 * @param parentCerPath 根证书
	 * @param sourceCerPath 源公钥证书
	 * @param sourcePfxPath 源私钥证书
	 */
	public static final void create(
			Organ issuer, Organ subject, String alias, Long validity, String password,
			String cerPath, String pfxPath, String parentCerPath,
			String sourceCerPath, String sourcePfxPath
		) {
		PublicKey publicKey;
		PrivateKey privateKey;
		OutputStream cerOutput = null;
		OutputStream pfxOutput = null;
		InputStream parentCerInput = null;
		try {
			if(StringUtils.isNotEmpty(sourceCerPath) && StringUtils.isNotEmpty(sourcePfxPath)) { // 加载文件
				publicKey = loadPublicKey(sourceCerPath);
				privateKey = loadPrivateKey(sourcePfxPath, password);
			} else {
				final KeyPair keyPair = keyPair();
				publicKey = keyPair.getPublic(); // 公钥
				privateKey = keyPair.getPrivate(); // 私钥
			}
			final Long currentTimeMillis = System.currentTimeMillis();
			final CertificateFactory certificateFactory = CertificateFactory.getInstance(CER_TYPE);
			final X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
				new X500Name(subject.message()), // 设置颁发者
				new BigInteger(String.valueOf(currentTimeMillis)), // 设置序列号
				new Date(currentTimeMillis), // 签发时间
				new Date(currentTimeMillis + validity), // 有效时间
				new X500Name(issuer.message()), // 设置使用者
				SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(publicKey.getEncoded())) // 证书信息
			);
			// 添加扩展
			if(StringUtils.isNotEmpty(parentCerPath)) {
				parentCerInput = new FileInputStream(parentCerPath);
				final Certificate parentCertificate = certificateFactory.generateCertificate(parentCerInput);
				builder.addExtension(
					Extension.authorityKeyIdentifier,
					false,
					SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(parentCertificate.getPublicKey().getEncoded()))
				);
			}
			// 添加签名
			final ContentSigner signer = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM).build(privateKey);
			final X509CertificateHolder holder = builder.build(signer);
			// 公钥生成
			cerOutput = new FileOutputStream(cerPath);
			cerOutput.write(holder.getEncoded());
			// 私钥生成
			final KeyStore keyStore = KeyStore.getInstance(PFX_TYPE);
			keyStore.load(null, password.toCharArray());
			final Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(holder.getEncoded()));
			keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), new Certificate[] { certificate });
			pfxOutput = new FileOutputStream(pfxPath);
			keyStore.store(pfxOutput, password.toCharArray());
		} catch (Exception e) {
			LOGGER.error("证书生成异常", e);
		} finally {
			close(parentCerInput);
			close(cerOutput);
			close(pfxOutput);
		}
	}
	
	/**
	 * 证书生成器
	 */
	public static final KeyPair keyPair() {
		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance(RSA_ALGORITHM); // 证书算法
			generator.initialize(KEY_SIZE); // 1024位
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("证书生成器异常", e);
		}
		return null;
	}
	
	/**
	 * 加载公钥
	 * @param path 公钥地址
	 * @return 公钥
	 */
	public static final PublicKey loadPublicKey(String path) {
		InputStream input = null;
		CertificateFactory certificatefactory = null;
		try {
			input = new FileInputStream(path);
			certificatefactory = CertificateFactory.getInstance(CER_TYPE);
			final Certificate certificate = certificatefactory.generateCertificate(input);
			return certificate.getPublicKey();
		} catch (Exception e) {
			LOGGER.error("加载公钥异常", e);
		} finally {
			close(input);
		}
		return null;
	}
	
	/**
	 * 加载密钥
	 * @param path 密钥地址
	 * @param password 密码
	 * @return 密钥
	 */
	public static final PrivateKey loadPrivateKey(String path, String password) {
		KeyStore keyStore = null;
		InputStream input = null;
		try {
			input = new FileInputStream(path);
			keyStore = KeyStore.getInstance(PFX_TYPE);
			keyStore.load(input, password.toCharArray());
			final Enumeration<String> aliases = keyStore.aliases();
			String aliase = null;
			while(aliases.hasMoreElements()) {
				aliase = aliases.nextElement();
			}
			return (PrivateKey) keyStore.getKey(aliase, password.toCharArray());
		} catch (Exception e) {
			LOGGER.error("加载私钥异常", e);
		} finally {
			close(input);
		}
		return null;
	}
	
	/**
	 * 公钥加密
	 * @param publicKey 公钥
	 * @param data 内容
	 * @return 加密后内容
	 */
	public static final String encrypt(PublicKey publicKey, String data) {
		return Base64.getEncoder().encodeToString(encrypt(publicKey, data.getBytes()));
	}
	
	/**
	 * 公钥加密
	 * @param publicKey 公钥
	 * @param data 内容
	 * @return 加密后内容
	 */
	private static final byte[] encrypt(PublicKey publicKey, byte[] data) {
		if(publicKey == null || data == null) {
			return null;
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int index = 0;
			byte[] tmp = null, old = null, encrypts = new byte[0];
			while(index < data.length) {
				old = encrypts;
				tmp = cipher.doFinal(data, index, index + ENCRYPT_LENGTH > data.length ? data.length - index : ENCRYPT_LENGTH);
				encrypts = new byte[old.length + tmp.length];
				System.arraycopy(old, 0, encrypts, 0, old.length);
				System.arraycopy(tmp, 0, encrypts, old.length, tmp.length);
				index += ENCRYPT_LENGTH;
			}
			return encrypts;
		} catch (Exception e) {
			LOGGER.error("加密异常", e);
		}
		return null;
	}
	
	/**
	 * 私钥加密
	 * @param privateKey 私钥
	 * @param data 内容
	 * @return 加密后内容
	 */
	@Deprecated
	public static final byte[] encrypt(PrivateKey privateKey, byte[] data) {
		if (privateKey == null || data == null) {
			return null;
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			int index = 0;
			byte[] tmp = null, old = null, encrypts = new byte[0];
			while(index < data.length) {
				old = encrypts;
				tmp = cipher.doFinal(data, index, index + ENCRYPT_LENGTH > data.length ? data.length - index : ENCRYPT_LENGTH);
				encrypts = new byte[old.length + tmp.length];
				System.arraycopy(old, 0, encrypts, 0, old.length);
				System.arraycopy(tmp, 0, encrypts, old.length, tmp.length);
				index += ENCRYPT_LENGTH;
			}
			return encrypts;
		} catch (Exception e) {
			LOGGER.error("加密异常", e);
		}
		return null;
	}
	
	/**
	 * 公钥解密
	 * @param publicKey 公钥 
	 * @param data 内容
	 * @return 解密后内容
	 */
	@Deprecated
	public static final byte[] decrypt(PublicKey publicKey, byte[] data) {
		if (publicKey == null || data == null) {
			return null;
		}
		Cipher cipher = null;
		try(ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
			cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			int index = 0;
			byte[] tmp = null;
			while(index < data.length) {
				tmp = cipher.doFinal(data, index, index + DECRYPT_LENGTH > data.length ? data.length - index : DECRYPT_LENGTH);
				index += DECRYPT_LENGTH;
				bytes.write(tmp, 0, tmp.length);
			}
			return bytes.toByteArray();
		} catch (Exception e) {
			LOGGER.error("解密异常", e);
		}
		return null;
	}
	
	/**
	 * 私钥解密
	 * @param privateKey 私钥
	 * @param data 内容
	 * @return 解密后内容
	 */
	public static final String decrypt(PrivateKey privateKey, String data) {
		return new String(decrypt(privateKey, Base64.getDecoder().decode(data)));
	}
	
	/**
	 * 私钥解密
	 * @param privateKey 私钥
	 * @param data 内容
	 * @return 解密后内容
	 */
	private static final byte[] decrypt(PrivateKey privateKey, byte[] data) {
		if (privateKey == null || data == null) {
			return null;
		}
		Cipher cipher = null;
		try(ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
			cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int index = 0;
			byte[] tmp = null;
			while(index < data.length) {
				tmp = cipher.doFinal(data, index, index + DECRYPT_LENGTH > data.length ? data.length - index : DECRYPT_LENGTH);
				index += DECRYPT_LENGTH;
				bytes.write(tmp, 0, tmp.length);
			}
			return bytes.toByteArray();
		} catch (Exception e) {
			LOGGER.error("解密异常", e);
		}
		return null;
	}
	
	/**
	 * 签名
	 * @param data 签名字符串
	 * @param privateKey 私钥
	 * @return 签名后字符串
	 */
	public static final String sign(String data, PrivateKey privateKey) {
		if (data == null || privateKey == null) {
			return null;
		}
		return Base64.getEncoder().encodeToString(sign(data.getBytes(), privateKey));
	}
	
	/**
	 * 签名
	 * @param data 签名字数据
	 * @param privateKey 私钥
	 * @return 签名后数据
	 */
	private static final byte[] sign(byte[] data, PrivateKey privateKey) {
		try {
			final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			LOGGER.error("签名异常", e);
		}
		return null;
	}
	
	/**
	 * 验签
	 * @param data 需要验证数据
	 * @param sign 签名后数据
	 * @param publicKey 公钥
	 * @return true：通过验证；false：验证失败
	 */
	public static final boolean verify(String data, String sign, PublicKey publicKey) {
		if (data == null || sign == null || publicKey == null) {
			return false;
		}
		return verify(data.getBytes(), Base64.getDecoder().decode(sign), publicKey);
	}
	
	/**
	 * 验签
	 * @param data 需要验证数据
	 * @param sign 签名后数据
	 * @param publicKey 公钥
	 * @return true：通过验证；false：验证失败
	 */
	private static final boolean verify(byte[] data, byte[] sign, PublicKey publicKey) {
		try {
			final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicKey);
			signature.update(data);
			return signature.verify(sign);
		} catch (Exception e) {
			LOGGER.error("验签异常", e);
		}
		return false;
	}
	
	/**
	 * 读取序列号
	 * @return 序列号
	 */
	public static final BigInteger readSerialNumber(String cerPath) {
		InputStream input = null;
		CertificateFactory certificateFactory;
		try {
			input = new FileInputStream(cerPath);
			certificateFactory = CertificateFactory.getInstance(CER_TYPE);
			final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(input);
			return certificate.getSerialNumber();
		} catch (CertificateException | FileNotFoundException e) {
			LOGGER.error("获取序列号异常", e);
		} finally {
			close(input);
		}
		return new BigInteger(Long.valueOf(System.currentTimeMillis()).toString());
	}
	
	/**
	 * 将公钥/私钥转换为BASE64编码的字符串
	 * @param key 公钥/私钥
	 * @return 编码后字符串
	 */
	public static final String keyToString(Key key) {
		if(key == null) {
			return null;
		}
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	/**
	 * 字符串转公钥
	 * @param key 字符串
	 * @return 公钥
	 */
	public static final PublicKey stringToPublicKey(String key) {
		final byte[] bytes = Base64.getDecoder().decode(key);
		final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
		try {
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			final PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("字符串转公钥异常，字符串内容：{}", key, e);
		}
		return null;
	}

	/**
	 * 字符串转私钥
	 * @param key 字符串
	 * @return 私钥
	 */
	public static final PrivateKey stringToPrivateKey(String key) {
		final byte[] bytes = Base64.getDecoder().decode(key);
		final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
		try {
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("字符串转私钥异常，字符串内容：{}", key, e);
		}
		return null;
	}

	/**
	 * 证书信息
	 */
	public static final Organ organ(String cn, String ou, String o, String l, String st, String c) {
		return new Organ(cn, ou, o, l, st, c);
	}
	
	/**
	 * 资源关闭
	 */
	private static final void close(Closeable closeable) {
		try {
			if(closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			closeable = null;
		}
	}
	
}

/**
 * 机构
 */
class Organ {
	
	private String cn; // 姓名和姓氏
	private String ou; // 组织单位名称
	private String o; // 组织名称
	private String l; // 城市或区域名称
	private String st; // 省/市/自治区名称
	private String c; // 单位的双字母国家/地区代码
	
	/**
	 * CN=姓名和姓氏, OU=组织单位名称, O=组织名称, L=城市或区域名称, ST=省/市/自治区名称, C=单位的双字母国家/地区代码
	 */
	public Organ(String cn, String ou, String o, String l, String st, String c) {
		this.cn = cn;
		this.ou = ou;
		this.o = o;
		this.l = l;
		this.st = st;
		this.c = c;
	}
	
	/**
	 * 机构信息
	 */
	public String message() {
		return "CN=" + cn + ", OU=" + ou + ", O=" + o + ", L=" + l + ", ST=" + st + ", C=" + c;
	}
	
	@Override
	public String toString() {
		return message();
	}
	
}