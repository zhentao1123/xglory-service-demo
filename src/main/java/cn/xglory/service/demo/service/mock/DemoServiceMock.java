package cn.xglory.service.demo.service.mock;

import org.springframework.stereotype.Component;

import cn.xglory.service.common.annotation.BizServiceMock;
import cn.xglory.service.common.exception.BaseServiceException;
import cn.xglory.service.demo.controller.request.HelloReq;
import cn.xglory.service.demo.controller.response.HelloRsp;
import cn.xglory.service.demo.service.DemoService;

@Component
@BizServiceMock
public class DemoServiceMock implements DemoService{

	@Override
	public HelloRsp hello(HelloReq req) throws BaseServiceException {
		return new HelloRsp("mock");
	}
	
}
