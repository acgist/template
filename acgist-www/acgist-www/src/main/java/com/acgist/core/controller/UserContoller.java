package com.acgist.core.controller;

import java.security.PrivateKey;

import javax.servlet.http.HttpServletRequest;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.config.AcgistConst;
import com.acgist.core.config.AcgistConstSession;
import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.core.pojo.message.TokenResultMessage;
import com.acgist.core.pojo.session.UserSession;
import com.acgist.core.service.IUserService;
import com.acgist.data.pojo.entity.UserEntity;
import com.acgist.data.pojo.message.LoginMessage;
import com.acgist.utils.PasswordUtils;
import com.acgist.utils.RsaUtils;

/**
 * <p>控制器 - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Controller
public class UserContoller {
	
	/**
	 * <p>登陆地址：{@value}</p>
	 */
	public static final String LOGIN = "/login";
	
	@Autowired
	private PrivateKey privateKey;
	@Reference(version = "${acgist.service.version}")
	private IUserService userService;

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		UserSession.logout(request);
		return "redirect:/";
	}
	
	/**
	 * <p>登陆页面</p>
	 * 
	 * @return 模板
	 */
	@GetMapping(LOGIN)
	public String login(String uri, Model model, HttpServletRequest request) {
		if(UserSession.exist(request)) {
			return "redirect:/user";
		}
		model.addAttribute("uri", uri);
		return "/login";
	}

	/**
	 * <p>提交登陆</p>
	 * 
	 * @param name 用户名称
	 * @param password 用户密码
	 * @param request 请求
	 * 
	 * @return 模板
	 */
	@PostMapping(LOGIN)
	@ResponseBody
	public TokenResultMessage login(String name, String password, HttpServletRequest request) {
		password = RsaUtils.decrypt(this.privateKey, password); // 解密
		final LoginMessage loginMessage = this.userService.login(name, password);
		final TokenResultMessage message = new TokenResultMessage();
		if(loginMessage.fail()) {
			message.setToken((String) request.getSession().getAttribute(AcgistConstSession.SESSION_CSRF_TOKEN));
			message.buildMessage(loginMessage);
		} else {
			final UserSession session = new UserSession();
			session.setId(loginMessage.getId());
			session.setName(loginMessage.getName());
			session.setNick(loginMessage.getNick());
			session.putSession(request);
			message.buildSuccess();
		}
		return message;
	}
	
	/**
	 * <p>注册页面</p>
	 * 
	 * @return 模板
	 */
	@GetMapping("/register")
	public String register(HttpServletRequest request) {
		if(UserSession.exist(request)) {
			return "redirect:/user";
		}
		return "/register";
	}
	
	/**
	 * <p>提交注册</p>
	 * 
	 * @param name 用户名称
	 * @param nick 用户昵称
	 * @param mail 用户邮箱
	 * @param mobile 用户手机
	 * @param password 用户密码
	 * @param code 邮箱验证码
	 * 
	 * @return 模板
	 */
	@PostMapping("/register")
	@ResponseBody
	public ResultMessage register(String name, String nick, String mail, String mobile, String password, String code) {
		password = RsaUtils.decrypt(this.privateKey, password); // 解密
		final UserEntity user = new UserEntity();
		user.setName(name);
		user.setNick(nick);
		user.setMail(mail);
		user.setMobile(mobile);
		user.setType(UserEntity.Type.USER);
		user.setPassword(PasswordUtils.encrypt(password));
		// TODO：默认角色
		this.userService.save(user);
		return ResultMessage.newInstance().buildSuccess();
	}
	
	/**
	 * <p>检查用户名称是否重复</p>
	 * 
	 * @param name 用户名称
	 * 
	 * @return 是否重复
	 */
	@GetMapping("/check/user/name")
	@ResponseBody
	public ResultMessage checkUserName(String name) {
		final ResultMessage message = new ResultMessage();
		if(name == null || name.length() < 4 || name.length() > 20) {
			return message.buildMessage(AcgistCode.CODE_9999, "用户名称格式错误");
		}
		if(this.userService.checkUserName(name)) {
			return message.buildSuccess();
		} else {
			return message.buildMessage(AcgistCode.CODE_9999, "用户名称已经注册");
		}
	}
	
	/**
	 * <p>检查用户邮箱是否重复</p>
	 * 
	 * @param mail 用户邮箱
	 * 
	 * @return 是否重复
	 */
	@GetMapping("/check/user/mail")
	@ResponseBody
	public ResultMessage checkUserMail(String mail) {
		final ResultMessage message = new ResultMessage();
		if(mail == null || !mail.matches(AcgistConst.MAIL_REGEX)) {
			return message.buildMessage(AcgistCode.CODE_9999, "用户邮箱格式错误");
		}
		if(this.userService.checkUserMail(mail)) {
			return message.buildSuccess();
		} else {
			return message.buildMessage(AcgistCode.CODE_9999, "用户邮箱已经注册");
		}
	}
	
	/**
	 * <p>发送注册邮件验证码</p>
	 * 
	 * @param mail 邮箱
	 * 
	 * @return 发送结果
	 */
	@GetMapping("/send/mail/code")
	@ResponseBody
	public ResultMessage sendMailCode(String mail) {
		// TODO：实现
		return null;
	}
	
}
