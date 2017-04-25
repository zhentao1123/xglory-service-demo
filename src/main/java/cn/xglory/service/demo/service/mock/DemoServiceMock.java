package cn.xglory.service.demo.service.mock;

import java.util.List;

import org.springframework.stereotype.Component;

import cn.xglory.service.common.controller.annotation.BizServiceMock;
import cn.xglory.service.demo.controller.request.HelloReq;
import cn.xglory.service.demo.controller.response.HelloRsp;
import cn.xglory.service.demo.entity.User;
import cn.xglory.service.demo.service.DemoService;

@Component
@BizServiceMock
public class DemoServiceMock implements DemoService{

	@Override
	public HelloRsp hello(HelloReq req) throws Exception {
		return new HelloRsp("mock");
	}

	@Override
	public Void createUser(User req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUserList() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
