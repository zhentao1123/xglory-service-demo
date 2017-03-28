package cn.xglory.service.demo.aop;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.xglory.service.common.annotation.BizController;
import cn.xglory.service.common.annotation.BizServiceImpl;
import cn.xglory.service.common.aop.BaseControllerAspect;
import cn.xglory.service.common.base.CommonReq;
import cn.xglory.service.common.base.CommonRsp;
import cn.xglory.service.util.spring.SpringUtils;

@Component
@Aspect
public class ControllerAspect extends BaseControllerAspect{
	
	/**
	 * the execution of any method defined in the controller package or a sub_package
	 */
	@Pointcut("execution(* cn.xglory.service.demo.controller..*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void processControllerMethod() {}
	
	/**
	 * 1.对接口调用做日志
	 * 2.对接口调用时间做记录、日志，并在返回对象中输出
	 */
	@Around("processControllerMethod()")
	public Object aroundControllerMethod(ProceedingJoinPoint pjp)
	{
		return super.aroundControllerMethod(pjp);
	}
	
	@Before("processControllerMethod()")
	public void beforeControllerMethod(){
		//logger.debug("== beforeControllerMethod ==");
	}
	
	@After("processControllerMethod()")
	public void afterControllerMethod(){
		//logger.debug("== afterControllerMethod ==");
	}
	
}
