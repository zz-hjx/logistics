package cn.zz.logistics.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zz.logistics.mo.MessageObject;
import cn.zz.logistics.pojo.Role;
import cn.zz.logistics.pojo.RoleExample;
import cn.zz.logistics.pojo.User;
import cn.zz.logistics.pojo.UserExample;
import cn.zz.logistics.pojo.UserExample.Criteria;
import cn.zz.logistics.service.RoleService;
import cn.zz.logistics.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleservice;
	@RequestMapping("/logout")
	public String logout() {
		
		return "redirect:/login.jsp";
	}
	
	

	@RequestMapping("/login")
	public String login(HttpServletRequest request,Model m) {
		// ��ȡ��֤ʧ�ܵĴ�����Ϣ����Shiro��ܵ� FormAuthenticationFilter �������й���
		// ������������� shiroLoginFailure
		// ����� shiro �쳣���ֽ��� ����
		String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
		System.out.println("�쳣���� ��" + shiroLoginFailure);
		if (shiroLoginFailure != null) {
			if (UnknownAccountException.class.getName().equals(shiroLoginFailure)) {
				m.addAttribute("erroyMsg", "�ס��˺Ų�����");
			} else if (IncorrectCredentialsException.class.getName().equals(shiroLoginFailure)) {
				m.addAttribute("erroyMsg", "�ס��������");
			}
		}
		return "forward:/login.jsp";
	}
	
	@RequestMapping("/adminPage")
	public  String adminPage() {
		return "adminPage";
	}
	@RequiresPermissions("admin:list")
	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<User> list(@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10")Integer pageSize,String keyword){
		PageHelper.startPage(pageNum, pageSize);
		System.out.println("123");
		//������ѯ����
		UserExample example = new UserExample();
		if (StringUtils.isNotBlank(keyword)) {
			Criteria criteria = example.createCriteria();
			criteria.andRealnameLike("%"+keyword+"%");
			Criteria criteria2 = example.createCriteria();
			criteria2.andUsernameLike("%"+keyword+"%");
			example.or(criteria2);
			
		}
		List<User> users= userService.selectByExample(example);
		for (User user : users) {
			System.out.println(user);
		}
		PageInfo<User> pageInfo = new PageInfo<>(users);
		
		
		return pageInfo;
		
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public MessageObject  delete(Long userId) {
		MessageObject mo= new MessageObject(0,"ɾ������ʧ��");
		System.out.println("123");
		int row = userService.deleteByPrimaryKey(userId);
		if (row==1) {
			mo=new MessageObject(1,"ɾ�����ݳɹ�");
		}
		return mo;
		
	}
	@RequestMapping("/edit")
	
	public String  edit(Model m,Long  userId) {
		System.out.println(userId);
		
		
		  if(userId != null) {
		  User user = userService.selectByPrimaryKey(userId);
		  m.addAttribute("user",user);
		  }
		 
		RoleExample example = new RoleExample();
		
		List<Role> roles = roleservice.selectByExample(example);
		for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			System.out.println(role.getRolename());
			
		}
		m.addAttribute("roles", roles);
		return "adminEdit";
		
	}
	@RequestMapping("/checkUsername")
	@ResponseBody
	public boolean checkUsername(String username) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<User> users = userService.selectByExample(example);
		
		
		return users.size()>0?false:true;
		
	}
	@RequestMapping("/insert")
	@ResponseBody	
		public MessageObject  insert(User user) {
		System.out.println("insert");
			MessageObject mo = new MessageObject(0,"���ʧ�ܣ����������");
			user.setCreateDate(new Date());		
			int i = userService.insert(user);
			if (i==1) {
				mo = new MessageObject(1,"������ݳɹ�");
			}
			return mo;
			
		}
		@RequestMapping("/update")
		@ResponseBody	
		public MessageObject  update(User user) {
		System.out.println(user);
			MessageObject mo = new MessageObject(0,"�޸�ʧ�ܣ��������޸�");
			int i = userService.updateByPrimaryKeySelective(user);
			if (i==1) {
				mo = new MessageObject(1,"�޸����ݳɹ�");
			}
			return mo;
			
		}
	
	
		
	
}
