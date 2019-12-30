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

import com.acgist.core.config.AcgistConstSession;
import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.core.pojo.message.TokenResultMessage;
import com.acgist.core.pojo.session.UserSession;
import com.acgist.core.service.IUserService;
import com.acgist.data.pojo.message.LoginMessage;
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
		model.addAttribute("token", request.getSession().getAttribute(AcgistConstSession.SESSION_CSRF_TOKEN));
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
	 * @param password 用户密码
	 * @param mail 邮箱
	 * @param code 邮箱验证码
	 * 
	 * @return 模板
	 */
	@PostMapping("/register")
	@ResponseBody
	public String register(String name, String password, String mail, String code) {
		return null;
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
		return null;
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
		return null;
	}
	
}
