package cn.xglory.service.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import cn.xglory.service.common.aop.BaseControllerAspect;

@Component
@Aspect
public class ControllerAspect extends BaseControllerAspect{
	
	/**
	 * the execution of any method defined in the controller package or a sub_package
	 */
	//高效模式(包目录匹配，限定接口controller层包目录，但BizController注释非必须)
	//@Pointcut("execution(* cn.xglory.service.demo.controller..*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	//通用模式(注释匹配，不限定接口controller层包目录，但BizController注释必须)
	@Pointcut("@annotation(cn.xglory.service.common.annotation.BizController)")
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
