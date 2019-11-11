package cn.zz.logistics.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zz.logistics.mapper.UserMapper;
import cn.zz.logistics.pojo.User;
import cn.zz.logistics.pojo.UserExample;
import cn.zz.logistics.pojo.UserExample.Criteria;
import cn.zz.logistics.service.UserService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class UserServiceTest {
	@Autowired
	private UserService userService;
	@Test
	public void testSelectByPrimaryKey() {
		
	User user = userService.selectByPrimaryKey((long) 1);
	System.out.println(user);
	}
	@Test
	public void testSelectByExample() {
		String keyWord = "ad";
		Integer pageNum=2;
		Integer pageSize= 10;
		PageHelper.startPage(pageNum, pageSize);
		UserExample example = new UserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUsernameLike("%"+keyWord+"%");
	  List<User> users= userService.selectByExample(example);
	  PageInfo<User> pageInfo = new PageInfo<>(users);
	  System.out.println(pageInfo.getPageSize());
	
	}
	

}
