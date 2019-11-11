package cn.zz.logistics.service;

import java.util.List;

import cn.zz.logistics.pojo.Permission;
import cn.zz.logistics.pojo.PermissionExample;

public interface PermissionService {
	int deleteByPrimaryKey(Long permissionId);

	int insert(Permission record);
	
	int updateByPrimaryKeySelective(Permission record);

	List<Permission> selectByExample(PermissionExample example);

	Permission selectByPrimaryKey(Long permissionId);

	List<String> selectPermissionByIds(List<Long> permissionIdsList);

	

}
