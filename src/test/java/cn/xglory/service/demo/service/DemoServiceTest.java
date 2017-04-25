package cn.xglory.service.demo.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import cn.xglory.service.demo.BaseTest;
import cn.xglory.service.demo.entity.User;
import cn.xglory.service.demo.service.impl.DemoServiceImpl;

public class DemoServiceTest extends BaseTest{

	@Autowired
	DemoServiceImpl service;
	
	@Test
	@Rollback(value = false)
	public void testCreateUser() throws Exception{
		User user = new User();
		user.setAge(12);
		user.setName("Tom");
		user.setBirthday(user.getSqlDate(new Date()));
		user.setSys_create_date(user.getSqlTimestamp(new Date()));
		boolean succeed = false;
		try{
			service.createUser(user);
			succeed = true;
		}catch(Exception e){}
		assertTrue(succeed);
	}
	
	@Test
	public void testGetUserList() throws Exception{
		List<User> userList = service.getUserList();
		assertNotNull(userList);
	}
}
