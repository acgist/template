package com.acgist.core.service.impl;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.core.service.IMailService;
import com.acgist.core.service.ISmsService;
import com.acgist.core.service.ITemplateService;
import com.acgist.core.service.IVerifyCodeService;
import com.acgist.data.service.RedisService;
import com.acgist.data.service.pojo.dto.MailDto;
import com.acgist.data.service.pojo.dto.SmsDto;
import com.acgist.data.service.pojo.entity.TemplateEntity;
import com.acgist.data.service.pojo.message.TemplateMessage;
import com.acgist.utils.RandomUtils;

/**
 * <p>服务 - 验证码</p>
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class VerifyCodeServiceImpl implements IVerifyCodeService {

	@Value("${acgist.verify.code.length:6}")
	private int length;
	@Value("${acgist.verify.code.duration:10}")
	private int duration;
	
	@Autowired
	private ISmsService smsService;
	@Autowired
	private IMailService mailService;
	@Autowired
	private RedisService redisService;
	@Reference(version = "${acgist.service.version}")
	private ITemplateService templateService;
	
	@Override
	public String build(String key) {
		final Random random = RandomUtils.buildRandom();
		final StringBuilder builder = new StringBuilder();
		for (int index = 0; index < this.length; index++) {
			builder.append(random.nextInt(10));
		}
		final String code = builder.toString();
		this.redisService.store(key, code, this.duration, TimeUnit.MINUTES);
		return code;
	}

	@Override
	public boolean buildMail(String mail) {
		final String code = this.build(mail);
		final MailDto mailDto = new MailDto();
		final TemplateMessage message = this.templateService.build(TemplateEntity.Type.CODE_MAIL, buildCodeTemplate(code));
		mailDto.setSubject(message.getName());
		mailDto.setContent(message.getContent());
		mailDto.setReceiver(mail);
		return this.mailService.send(mailDto);
	}

	@Override
	public boolean buildMobile(String mobile) {
		final String code = this.build(mobile);
		final SmsDto smsDto = new SmsDto();
		final TemplateMessage message = this.templateService.build(TemplateEntity.Type.CODE_SMS, buildCodeTemplate(code));
		smsDto.setMobile(mobile);
		smsDto.setContent(message.getContent());
		return this.smsService.send(smsDto);
	}
	
	@Override
	public ResultMessage verify(String key, String code) {
		final String trueCode = this.redisService.get(key);
		if(trueCode == null) {
			return ResultMessage.newInstance().buildMessage(AcgistCode.CODE_9999, "验证码失效");
		}
		if(trueCode.equalsIgnoreCase(code)) {
			return ResultMessage.newInstance().buildSuccess();
		}
		return ResultMessage.newInstance().buildMessage(AcgistCode.CODE_9999, "验证码错误");
	}
	
	/**
	 * <p>创建模板数据</p>
	 * 
	 * @param code 验证码
	 * 
	 * @return 模板数据
	 */
	private Map<String, Object> buildCodeTemplate(String code) {
		return Map.of("code", code);
	}

}
