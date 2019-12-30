package com.acgist.core.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acgist.core.pojo.session.UserSession;
import com.acgist.core.service.IUserService;
import com.acgist.data.pojo.entity.UserEntity;
import com.acgist.data.repository.UserRepository;

/**
 * <p>控制器 - 用户中心</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Controller
@RequestMapping("/user")
public class CenterController {

	@Reference(version = "${acgist.service.version}")
	private IUserService userService;
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * <p>用户中心</p>
	 * 
	 * @param model model
	 * @param request 请求
	 * 
	 * @return 模板
	 */
	@GetMapping
	public String index(Model model, HttpServletRequest request) {
		final UserSession session = UserSession.get(request);
		model.addAttribute("user", this.userRepository.findOne(session.getId()));
		return "/user/index";
	}

	/**
	 * <p>修改用户信息</p>
	 * 
	 * @param nick 昵称
	 * @param request 请求
	 * 
	 * @return 模板
	 */
	@PostMapping("/update")
	public String update(String nick, HttpServletRequest request) {
		final UserEntity entity = new UserEntity();
		final UserSession session = UserSession.get(request);
		entity.setNick(nick);
		entity.setName(session.getName());
		this.userService.update(entity);
		session.setNick(nick);
		session.putSession(request);
		return "redirect:/user";
	}
	
}
