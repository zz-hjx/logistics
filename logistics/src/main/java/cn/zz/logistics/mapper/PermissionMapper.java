package cn.zz.logistics.mapper;

import cn.zz.logistics.pojo.Permission;
import cn.zz.logistics.pojo.PermissionExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long permissionId);

    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> selectByExample(PermissionExample example);

    Permission selectByPrimaryKey(Long permissionId);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

	List<String> selectPermissionByIds(@Param("permissionIds")List<Long> permissionIdsList);
}