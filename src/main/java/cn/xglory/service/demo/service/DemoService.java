package cn.xglory.service.demo.service;

import java.util.List;

import cn.xglory.service.common.controller.annotation.BizService;
import cn.xglory.service.demo.controller.request.HelloReq;
import cn.xglory.service.demo.controller.response.HelloRsp;
import cn.xglory.service.demo.entity.User;

@BizService
public interface DemoService{
	
	public HelloRsp hello(HelloReq req) throws Exception;
	
	public Void createUser(User req) throws Exception;
	
	public List<User> getUserList() throws Exception;
}
