package cn.xglory.service.demo.aop;

import java.util.Map;

import org.junit.Test;

import cn.xglory.service.common.annotation.BizService;
import cn.xglory.service.demo.BaseTest;
import cn.xglory.service.demo.service.mock.DemoServiceMock;
import cn.xglory.service.util.spring.SpringUtils;

public class ControllerAspectTest extends BaseTest {
	
	@Test
	public void test1() throws ClassNotFoundException{
		/*
		String[] beanNames = SpringUtils.getContext().getBeanNamesForType(Class.forName("cn.xglory.service.demo.service.DemoService"));
		for(String name : beanNames){
			System.out.println(name);
		}
		DemoServiceMock bean = SpringUtils.getContext().getBean(DemoServiceMock.class);
		System.out.println(bean!=null);
		*/
		
		Map<String, Object> beanMap = SpringUtils.getContext().getBeansWithAnnotation(BizService.class);
		for(String key : beanMap.keySet()){
			System.out.println(key);
		}
		
		
	}
}
