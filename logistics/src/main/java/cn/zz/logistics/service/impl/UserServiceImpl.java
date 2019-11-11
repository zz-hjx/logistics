package cn.zz.logistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zz.logistics.mapper.UserMapper;
import cn.zz.logistics.pojo.User;
import cn.zz.logistics.pojo.UserExample;
import cn.zz.logistics.service.UserService;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public int deleteByPrimaryKey(Long userId) {
		
		return userMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(User record) {
		
		return userMapper.insert(record);
	}

	@Override
	public int insertSelective(User record) {
		
		return userMapper.insertSelective(record);
	}

	@Override
	public List<User> selectByExample(UserExample example) {
		
		return userMapper.selectByExample(example);
	}

	@Override
	public User selectByPrimaryKey(Long userId) {
		
		return userMapper.selectByPrimaryKey(userId);
	}
	@Override
	public User selectByUsername(String username) {
		
		return userMapper.selectByUsername(username);
	}

	@Override
	public int updateByPrimaryKeySelective(User record) {
		
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

}
