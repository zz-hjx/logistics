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
		 * �Զ�����֤����
		 * �Զ�����֤˼·
		 * ------------------
		 * 1, ע��UserService Servc��
		 *  	@Autowired
		 *  	public UserService userService;
		 * 2����token��ȡ��ݣ��˺ţ���Ϣ
		 * 		String username = (String) token.getPrincipal();
		 * 3,���� userService ������˺Ų�ѯ���û���Ϣ�ķ���
		 *  	User user = userService.selectByUsername(username);
		 *    3.1 user ���� null ��ǰ����ֱ�ӷ��� һ��null
		 *    
		 *    3.2 user ����null ��˵���˺Ŵ���
		 *    
		 *  4, ������֤��Ϣ����  ������ݣ����ݲ�ѯ���Ǹ� user���󣬺�user�����е����루ƾ֤����Ϊ�������ݽ�ȥ��
		 *  	String credentials(password) = user.getPassword();
		 *   	return new SimpleAuthenticationInfo(principals, credentials, realmName)
		 *     Shiro����ڲ� ��� �û����ݹ��� token�����ƣ��е������  ���ݿ��в�ѯ�������������ƥ��
		 *     ���ƥ��ɹ�����֤ͨ����ƥ��ʧ�ܣ���֤ʧ��
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
		 * �Զ�����Ȩ����
		 * 
		 * �Զ�����Ȩ˼·
		 * ------------------------------------
		 * 
		 *1�� ��ȡ��ݵĽ�ɫ�� Ȩ��id 
		 * User user = principals.getPrimaryPrincipal();
		 * String roleId = user.getPermissionIds; //1,3,5,8,9,10...
		 * 
		 *2,�и�Ȩ��id�ַ�����ȡÿһ��Ȩ�޵�idֵ
		 *
		 *3������ÿһ��Ȩ�޵�idֵ��ȡ��Ӧ��Ȩ�ޱ��ʽ,
		 * List<String> permissions = permissionService.serlectPermissionByIds();
		 *	�磺
		 *		user:list
		 *		user:insert
		 *		role:delete
		 *		role:update
		 *		....
		 *	
		 * 4,������Ȩ��Ϣ����
		 * 	SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		 * 
		 * 5������������ѯ����Ȩ�ޱ��ʽ��ӵ�Ȩ��Ȩ����Ϣ����
		 * 		authorizationInfo.addStringPermission("user:list");
		 * 
		 * 6����������ʱ��������Ȩ���жϵط���ȡȨ�ޱ��ʽ�� ����Shiro���Ȩ�ޱ��ʽ���ݽ��бȶ�
		 * 	�У�����
		 *  û�У�������
		 */
		User user = (User) principals.getPrimaryPrincipal();
		String permissionIds = user.getPermissionIds();
		/* ת��Ϊ���� */
		String[] splits = permissionIds.split(",");
		/* ת��Ϊ���� */
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
