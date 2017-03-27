package cn.xglory.service.demo.aop;

import java.math.BigInteger;
import java.util.Date;

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

import cn.xglory.service.common.base.CommonReq;
import cn.xglory.service.common.base.CommonRsp;
import cn.xglory.service.util.spring.SpringUtils;

@Component
@Aspect
public class ControllerAspect {
	
	private static Log logger = LogFactory.getLog(ControllerAspect.class);
	
	/**
	 * the execution of any method defined in the controller package or a sub_package
	 */
	@Pointcut("execution(* cn.xglory.service.demo.controller..*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void processControllerMethod() {}
	
	@Before("processControllerMethod()")
	public void beforeControllerMethod(){
		//logger.debug("== beforeControllerMethod ==");
	}
	
	@After("processControllerMethod()")
	public void afterControllerMethod(){
		//logger.debug("== afterControllerMethod ==");
	}
	
	/**
	 * 1.对接口调用做日志
	 * 2.对接口调用时间做记录、日志，并在返回对象中输出
	 */
	@Around("processControllerMethod()")
	public Object aroundControllerMethod(ProceedingJoinPoint pjp)
	{
		long processBegin = new Date().getTime();
		logger.debug("== aroundControllerMethod begin ==");
		//logger.debug("RequestMapping : " + requestMapping.value());
		
		Object[] args = pjp.getArgs();
		Object result = null;
		CommonReq<?> req = null;
		CommonRsp<?> rsp = null;
		
		
		System.out.println(pjp.getArgs());
		System.out.println(pjp.getKind());
		System.out.println(pjp.getTarget());
		System.out.println(pjp.getTarget().getClass().getSimpleName());
		System.out.println(pjp.getThis());
		System.out.println(pjp.toLongString());
		System.out.println(pjp.getSignature());
		System.out.println(pjp.getSignature().getDeclaringType());
		System.out.println(pjp.getSourceLocation());
		System.out.println(pjp.getStaticPart());
		
		
		//打印接口URL日志
		String path = "";
		String methodPath = "";
		String classPath = "";
		
		RequestMapping classRequestMapping = (RequestMapping)pjp.getTarget().getClass().getAnnotation(RequestMapping.class);
			classPath = classRequestMapping.value()[0];
		MethodSignature methoSignature = (MethodSignature)pjp.getSignature();
		RequestMapping methodRequestMapping = (RequestMapping)methoSignature.getMethod().getAnnotation(RequestMapping.class);
			methodPath = methodRequestMapping.value()[0];
		path = classPath + methodPath;
		logger.debug("path : " + path);
		
		//处理参数
		try{
			req = (CommonReq<?>)args[0];
			//req.setMock(true);
		}catch(Exception e){}
		
		//调用处理
		try {
			result = pjp.proceed(args);
			rsp = (CommonRsp<?>)result;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		//处理返回值
		long processEnd = new Date().getTime();
		long duration = processEnd - processBegin;
		try{
			rsp.setTime(BigInteger.valueOf(duration));
		}catch(Exception e){}
		logger.debug("duration : " + duration);
		
		logger.debug("== aroundControllerMethod end ==");
		return rsp;
	}
}
