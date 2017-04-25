package cn.xglory.service.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.xglory.service.common.controller.annotation.BizServiceImpl;
import cn.xglory.service.demo.controller.request.HelloReq;
import cn.xglory.service.demo.controller.response.HelloRsp;
import cn.xglory.service.demo.dao.entitydao.UserDao;
import cn.xglory.service.demo.entity.User;
import cn.xglory.service.demo.service.DemoService;

@Component
@BizServiceImpl
public class DemoServiceImpl implements DemoService{

	@Autowired
	UserDao userDao;
	
	@Override
	public HelloRsp hello(HelloReq req) throws Exception {
		return new HelloRsp("hello");
	}

	@Override
	public Void createUser(User req) throws Exception {
		userDao.insertEntity(req);
		return null; 
	}

	@Override
	public List<User> getUserList() throws Exception {
		return userDao.getAllEntity();
	}

}
