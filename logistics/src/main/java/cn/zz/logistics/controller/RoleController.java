package cn.zz.logistics.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import cn.zz.logistics.pojo.RoleExample;
import cn.zz.logistics.pojo.Role;
import cn.zz.logistics.pojo.RoleExample;
import cn.zz.logistics.pojo.RoleExample.Criteria;
import cn.zz.logistics.service.RoleService;
import cn.zz.logistics.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleService roleservice;
	
	@RequestMapping("/rolePage")
	public  String rolePage() {
		return "rolePage";
	}
	@RequestMapping("/list")
	
	@ResponseBody
	public PageInfo<Role> list(@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10")Integer pageSize,String keyword){
		PageHelper.startPage(pageNum, pageSize);
		System.out.println("123");
		//条件查询对象
		RoleExample example = new RoleExample();
		if (StringUtils.isNotBlank(keyword)) {
			cn.zz.logistics.pojo.RoleExample.Criteria criteria = example.createCriteria();
			criteria.andRolenameLike("%"+keyword+"%");
			
		}
		 List<Role> roles = roleService.selectByExample(example);
		for (Role role :roles ) {
			System.out.println(role);
		}
		PageInfo<Role> pageInfo = new PageInfo<>(roles);
		return pageInfo;
		
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public MessageObject  delete(Long roleId) {
		MessageObject mo= new MessageObject(0,"删除数据失败");
		System.out.println("123");
		int row = roleService.deleteByPrimaryKey(roleId);
		if (row==1) {
			mo=new MessageObject(1,"删除数据成功");
		}
		return mo;
		
	}
	@RequestMapping("/edit")
	
	public String  edit(Model m,Long  roleId) {
		System.out.println(roleId);
		
		
		  if(roleId != null) {
		  Role role = roleService.selectByPrimaryKey(roleId);
		  m.addAttribute("role",role);
		  }
		 
		RoleExample example = new RoleExample();
			
		 List<Role> roles = roleService.selectByExample(example);
	for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
		Role role = (Role) iterator.next();
		System.out.println(role.getRemark());
	}
		m.addAttribute("roles", roles);
		return "roleEdit";
		
	}

	/*
	 * @RequestMapping("/checkRolename")
	 * 
	 * @ResponseBody public boolean checkRolename(String name) { RoleExample example
	 * = new RoleExample(); Criteria criteria = example.createCriteria();
	 * criteria.andNameEqualTo(name); List<Role> roles =
	 * roleService.selectByExample(example); System.out.println(roles);
	 * 
	 * return roles.size()>0?false:true;
	 * 
	 * }
	 */
	@RequestMapping("/insert")
	@ResponseBody	
		public MessageObject  insert(Role role) {
		System.out.println("insert");
			MessageObject mo = new MessageObject(0,"添加失败，请重新添加");
			
			int i = roleService.insert(role);
			if (i==1) {
				mo = new MessageObject(1,"添加数据成功");
			}
			return mo;
			
		}
		@RequestMapping("/update")
		@ResponseBody	
		public MessageObject  update(Role role) {
		System.out.println(role);
			MessageObject mo = new MessageObject(0,"修改失败，请重新修改");
			int i = roleService.updateByPrimaryKeySelective(role);
			if (i==1) {
				mo = new MessageObject(1,"修改数据成功");
			}
			return mo;
			
		}
	
	
		
	
}
