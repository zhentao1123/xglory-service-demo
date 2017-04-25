package cn.xglory.service.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xglory.service.common.controller.annotation.BizController;
import cn.xglory.service.common.controller.base.BaseServiceController;
import cn.xglory.service.common.controller.base.CommonReq;
import cn.xglory.service.common.controller.base.CommonRsp;
import cn.xglory.service.demo.controller.request.HelloReq;
import cn.xglory.service.demo.controller.response.HelloRsp;
import cn.xglory.service.demo.entity.User;

@Controller
@RequestMapping(value = "/service/demo/", method = RequestMethod.POST)
@BizController(serviceImplClass = "DemoServiceImpl", serviceMockClass = "DemoServiceMock")
//@BizController
public class DemoController extends BaseServiceController {
	
	@RequestMapping(value = "hello")
	@ResponseBody
	@BizController(serviceMethodName="hello")
	public CommonRsp<HelloRsp> hello(@RequestBody CommonReq<HelloReq> req) throws Exception{
		return null;
	}
	
	@RequestMapping(value = "createUser")
	@ResponseBody
	@BizController
	public CommonRsp<Void> createUser(@RequestBody CommonReq<User> req) throws Exception {
		return null; 
	}

	@RequestMapping(value = "getUserList")
	@ResponseBody
	@BizController
	public CommonRsp<List<User>> getUserList(@RequestBody CommonReq<Void> req) throws Exception {
		return null;
	}
}
