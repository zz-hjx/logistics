package cn.zz.logistics.service;

import java.util.List;

import cn.zz.logistics.pojo.User;
import cn.zz.logistics.pojo.UserExample;

public interface UserService {
	 int deleteByPrimaryKey(Long userId);

	    int insert(User record);

	    int insertSelective(User record);

	    List<User> selectByExample(UserExample example);

	    User selectByPrimaryKey(Long userId);

	    int updateByPrimaryKeySelective(User record);

	    int updateByPrimaryKey(User record);

		User selectByUsername(String username);

}
