package cn.xglory.service.demo.controller.response;

import cn.xglory.service.common.base.BaseResponseData;

public class HelloRsp extends BaseResponseData{
	private String greetings;
	
	public HelloRsp() {
		super();
	}

	public HelloRsp(String greetings) {
		super();
		this.greetings = greetings;
	}

	public String getGreetings() {
		return greetings;
	}

	public void setGreetings(String greetings) {
		this.greetings = greetings;
	}
	
}
