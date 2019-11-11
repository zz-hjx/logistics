package cn.zz.logistics.shiro;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zz.logistics.pojo.Permission;
import cn.zz.logistics.pojo.PermissionExample;
import cn.zz.logistics.pojo.User;
import cn.zz.logistics.pojo.UserExample;
import cn.zz.logistics.pojo.UserExample.Criteria;
import cn.zz.logistics.service.PermissionService;
import cn.zz.logistics.service.RoleService;
import cn.zz.logistics.service.UserService;

public class MyRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PermissionService permissionService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		/*
		 * 自定义认证方法
		 * 自定义认证思路
		 * ------------------
		 * 1, 注入UserService Servc层
		 *  	@Autowired
		 *  	public UserService userService;
		 * 2，从token获取身份（账号）信息
		 * 		String username = (String) token.getPrincipal();
		 * 3,调用 userService 层根据账号查询出用户信息的方法
		 *  	User user = userService.selectByUsername(username);
		 *    3.1 user 等于 null 当前方法直接返回 一个null
		 *    
		 *    3.2 user 不能null ，说明账号存在
		 *    
		 *  4, 创建认证信息对象  ，将身份（数据查询的那个 user对象，和user对象中的密码（凭证）作为参数传递进去）
		 *  	String credentials(password) = user.getPassword();
		 *   	return new SimpleAuthenticationInfo(principals, credentials, realmName)
		 *     Shiro框架内部 会把 用户传递过来 token（令牌）中的密码和  数据库中查询出来的密码进行匹配
		 *     如果匹配成功，认证通过，匹配失败，认证失败
		 * 
		 */
		String  username = (String) token.getPrincipal();
	
		User user = userService.selectByUsername(username);
		System.out.println(user); 
		
		
		  if (user == null) { return null; }
		 
		String password = user.getPassword();
		String salt = user.getSalt();
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, password, ByteSource.Util.bytes(salt), this.getName());
		System.out.println(authenticationInfo);
		
		return authenticationInfo;
	}
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*
		 * 自定义授权方法
		 * 
		 * 自定义授权思路
		 * ------------------------------------
		 * 
		 *1， 获取身份的角色的 权限id 
		 * User user = principals.getPrimaryPrincipal();
		 * String roleId = user.getPermissionIds; //1,3,5,8,9,10...
		 * 
		 *2,切割权限id字符串获取每一个权限的id值
		 *
		 *3，根据每一个权限的id值获取对应的权限表达式,
		 * List<String> permissions = permissionService.serlectPermissionByIds();
		 *	如：
		 *		user:list
		 *		user:insert
		 *		role:delete
		 *		role:update
		 *		....
		 *	
		 * 4,创建授权信息对象
		 * 	SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		 * 
		 * 5，将第三步查询出的权限表达式添加到权限权限信息对中
		 * 		authorizationInfo.addStringPermission("user:list");
		 * 
		 * 6，程序运行时候会根据有权限判断地方获取权限表达式和 设置Shiro框架权限表达式数据进行比对
		 * 	有：放行
		 *  没有：不放行
		 */
		User user = (User) principals.getPrimaryPrincipal();
		String permissionIds = user.getPermissionIds();
		/* 转化为数组 */
		String[] splits = permissionIds.split(",");
		/* 转化为集合 */
		List<Long> permissionIdsList = new ArrayList<>();
		for (String  split : splits) {
			permissionIdsList.add(Long.valueOf(split));
		}
		/* permissionService.selectPermissionByIds(list); */
		PermissionExample example = new PermissionExample();
		cn.zz.logistics.pojo.PermissionExample.Criteria criteria = example.createCriteria();
		List<Permission> permissions = permissionService.selectByExample(example);
		
		criteria.andPermissionIdIn(permissionIdsList);
		System.out.println(permissionIdsList);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		for (Permission permission : permissions) {
			String expression = permission.getExpression();
			authorizationInfo.addStringPermission(expression);
		}
				
		return authorizationInfo;
	}


}
