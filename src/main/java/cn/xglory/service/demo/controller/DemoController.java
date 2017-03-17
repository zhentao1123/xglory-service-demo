package cn.xglory.service.demo.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xglory.service.common.base.BaseServiceController;
import cn.xglory.service.common.base.CommonReq;
import cn.xglory.service.common.base.CommonRsp;
import cn.xglory.service.demo.controller.request.HelloReq;
import cn.xglory.service.demo.controller.response.HelloRsp;

@Controller
@RequestMapping(value = "/service/", method = RequestMethod.POST)
public class DemoController extends BaseServiceController {
	
	private static Log logger = LogFactory.getLog(DemoController.class);
	
	@RequestMapping(value = "hello")
	@ResponseBody
	public CommonRsp<HelloRsp> hello(@RequestBody CommonReq<HelloReq> req) throws Exception{
		return null;
	}
}
