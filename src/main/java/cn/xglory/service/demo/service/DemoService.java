package cn.xglory.service.demo.service;

import cn.xglory.service.common.exception.BaseServiceException;
import cn.xglory.service.demo.controller.request.HelloReq;
import cn.xglory.service.demo.controller.response.HelloRsp;

public interface DemoService{
	
	public HelloRsp hello(HelloReq req) throws BaseServiceException;
}
