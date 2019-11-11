package cn.zz.logistics.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.zz.logistics.pojo.User;
import cn.zz.logistics.service.UserService;

@Controller

public class IndexController {
	@Autowired
	private UserService userService;
	@RequestMapping("/index")
	public String index(Model m) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		m.addAttribute("user",user);
		
		return "index";
		
	}
	@RequestMapping("/welcome")
	public String welcome(){
		return "welcome";
		
	}

}
