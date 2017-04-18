package cn.xglory.service.demo.controller.request;

import cn.xglory.service.common.controller.base.BaseAccountReqData;

public class HelloReq extends BaseAccountReqData{
	
	private String name;
	private String word;
	
	public HelloReq() {
		super();
	}
	
	public HelloReq(String name, String word) {
		super();
		this.name = name;
		this.word = word;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
}
