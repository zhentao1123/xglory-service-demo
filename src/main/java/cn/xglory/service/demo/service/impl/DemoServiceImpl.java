package cn.xglory.service.demo.service.impl;

import org.springframework.stereotype.Component;

import cn.xglory.service.common.controller.annotation.BizServiceImpl;
import cn.xglory.service.common.exception.BaseServiceException;
import cn.xglory.service.demo.controller.request.HelloReq;
import cn.xglory.service.demo.controller.response.HelloRsp;
import cn.xglory.service.demo.service.DemoService;

@Component
@BizServiceImpl
public class DemoServiceImpl implements DemoService{

	@Override
	public HelloRsp hello(HelloReq req) throws BaseServiceException {
		return new HelloRsp("hello");
	}

}
