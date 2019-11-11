package cn.zz.logistics.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import cn.zz.logistics.pojo.PermissionExample;
import cn.zz.logistics.pojo.Permission;
import cn.zz.logistics.pojo.PermissionExample;
import cn.zz.logistics.pojo.PermissionExample.Criteria;
import cn.zz.logistics.service.RoleService;
import cn.zz.logistics.service.PermissionService;

@Controller
@RequestMapping("/permission")
public class PermissionController {
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RoleService roleservice;
	
	@RequestMapping("/permissionPage")
	public  String permissionPage() {
		return "permissionPage";
	}
	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<Permission> list(@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10")Integer pageSize,String keyword){
		PageHelper.startPage(pageNum, pageSize);
		System.out.println("123");
		//条件查询对象
		PermissionExample example = new PermissionExample();
		if (StringUtils.isNotBlank(keyword)) {
			Criteria criteria = example.createCriteria();
			criteria.andNameLike("%"+keyword+"%");
			
		}
		List<Permission> permissions = permissionService.selectByExample(example);
		for (Permission permission : permissions) {
			System.out.println(permission);
		}
		PageInfo<Permission> pageInfo = new PageInfo<>(permissions);
		
		
		return pageInfo;
		
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public MessageObject  delete(Long permissionId) {
		MessageObject mo= new MessageObject(0,"删除数据失败");
		System.out.println("123");
		int row = permissionService.deleteByPrimaryKey(permissionId);
		if (row==1) {
			mo=new MessageObject(1,"删除数据成功");
		}
		return mo;
		
	}
	@RequestMapping("/edit")
	
	public String  edit(Model m,Long  permissionId) {
		System.out.println(permissionId);
		
		
		  if(permissionId != null) {
		  Permission permission = permissionService.selectByPrimaryKey(permissionId);
		  m.addAttribute("permission",permission);
		  }
		 
		PermissionExample example = new PermissionExample();
		
		 List<Permission> permissions = permissionService.selectByExample(example);
	for (Iterator iterator = permissions.iterator(); iterator.hasNext();) {
		Permission permission = (Permission) iterator.next();
		System.out.println(permission.getParentName());
	}
		m.addAttribute("permissions", permissions);
		return "permissionEdit";
		
	}
	@RequestMapping("/checkPermissionname")
	@ResponseBody
	public boolean checkPermissionname(String name) {
		PermissionExample example = new PermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		List<Permission> permissions = permissionService.selectByExample(example);
		System.out.println(permissions);
		
		return permissions.size()>0?false:true;
		
	}
	@RequestMapping("/insert")
	@ResponseBody	
		public MessageObject  insert(Permission permission) {
		System.out.println("insert");
			MessageObject mo = new MessageObject(0,"添加失败，请重新添加");
			
			int i = permissionService.insert(permission);
			if (i==1) {
				mo = new MessageObject(1,"添加数据成功");
			}
			return mo;
			
		}
		@RequestMapping("/update")
		@ResponseBody	
		public MessageObject  update(Permission permission) {
		System.out.println(permission);
			MessageObject mo = new MessageObject(0,"修改失败，请重新修改");
			int i = permissionService.updateByPrimaryKeySelective(permission);
			if (i==1) {
				mo = new MessageObject(1,"修改数据成功");
			}
			return mo;
			
		}
	
	
		
	
}
