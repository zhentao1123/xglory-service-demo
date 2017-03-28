package cn.xglory.service.common.aop;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.xglory.service.common.base.CommonReq;
import cn.xglory.service.common.base.CommonRsp;
import cn.xglory.service.util.spring.SpringUtils;

public class BaseControllerAspect {
	
	private static Log logger = LogFactory.getLog(BaseControllerAspect.class);
	
	protected Object aroundControllerMethod(ProceedingJoinPoint pjp, String defaultServiceImpPackage, String defaultServiceMockPackage)
	{
		long processBegin = new Date().getTime();
		logger.debug("== aroundControllerMethod begin ==");
		//logger.debug("RequestMapping : " + requestMapping.value());
		
		Object[] args = pjp.getArgs();
		Object result = null;
		CommonReq<Object> req = null;
		CommonRsp<Object> rsp = null;
		
		
		//System.out.println(pjp.getArgs());
		//System.out.println(pjp.getKind());
		//System.out.println(pjp.getTarget());
		//System.out.println(pjp.getTarget().getClass().getSimpleName());
		//System.out.println(pjp.getThis());
		//System.out.println(pjp.toLongString());
		
		System.out.println(pjp.getSignature());
		System.out.println(pjp.getSignature().getName());
		System.out.println(pjp.getSignature().getDeclaringType());
		System.out.println(pjp.getSignature().getDeclaringTypeName());
		System.out.println(pjp.getSignature().toShortString());
		System.out.println(pjp.getSignature().toLongString());
		
		//System.out.println(pjp.getSourceLocation());
		//System.out.println(pjp.getStaticPart());
		
		
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
			req = (CommonReq<Object>)args[0];
			if(null==req.isMock()){ //默认调用真实实现
				req.setMock(false);
			}
		}catch(Exception e){}
		
		//调用处理
		try {
			//result = pjp.proceed(args);
			//rsp = (CommonRsp<?>)result;
			
			String serviceImplName = null;
			String serviceMockName = null;
			Class serviceClass = null;
			Method serviceMethod = null;
			
			//搜索匹配的Service
			Class targetClazz = pjp.getTarget().getClass();//当前横切目标类
			//获取仅根据类名约定关系匹配的Service类名，作为没有标签情况的默认值；没有标签不项目中不能再定义同名类
			String defaultServiceImplName = null;
			String defaultServiceMockName = null;
			String defaultServiceMethodName = pjp.getSignature().getName();
			Method defaultServiceMethod = null;
			try{
				defaultServiceImplName = targetClazz.getSimpleName().replace("Controller", "ServiceImpl");
				serviceImplName = defaultServiceImplName;
			}catch(Exception e){}
			try{
				defaultServiceMockName = targetClazz.getSimpleName().replace("Controller", "ServiceMock");
				serviceMockName = defaultServiceMockName;
			}catch(Exception e){}
			try{
				//defaultServiceMethod = getClassMethod(targetClazz, defaultServiceMethodName, null);
				serviceMethod = defaultServiceMethod;
			}catch(Exception e){}
			
			//获取BizController标签指定的业务实现类及业务模拟类
			/*
			
			Object obj = clazz.getAnnotation(BizController.class);
			if(null!=obj){
				BizController bizController = (BizController)obj;
				serviceImplName = bizController.serviceImplClass();
				if(StringUtils.isBlank(serviceImplName)){
					serviceImplName = defaultServiceImplName;
				}
				serviceMockName = bizController.serviceMockClass();
				if(StringUtils.isBlank(serviceMockName)){
					serviceMockName = defaultServiceMockName;
				}
			}
			if(null!=serviceImplName){
				
			}else{
				//封装固定异常返回 TODO
			}
			*/
			
			
			
			//搜索匹配的Method
			
			
			
			
			//调用服务
			Object invokeResult = null;
			if(!req.isMock()){
				serviceClass = SpringUtils.getContext().getClassLoader().loadClass(defaultServiceImpPackage+serviceImplName);
			}else{
				serviceClass = SpringUtils.getContext().getClassLoader().loadClass(defaultServiceMockPackage+serviceMockName);
			}
			serviceMethod = getClassMethod(serviceClass, defaultServiceMethodName, null);
			invokeResult = serviceMethod.invoke(SpringUtils.getBean(serviceClass), req.getData());
			
			rsp = new CommonRsp<Object>();
			rsp.setData(invokeResult);;
			rsp.init4Succeed();
			
		} catch (Throwable e) {
			e.printStackTrace();
			//封装固定异常返回 TODO
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
	
	/**
	 * 根据方法名（和参数类型）获取类中的方法
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	private Method getClassMethod(Class clazz, String methodName, Class... parameterTypes){
		Method method = null;
		try{
			if(null!=parameterTypes){
				method = clazz.getMethod(methodName, parameterTypes);
			}else{
				Method[] methods = clazz.getMethods();
				for(Method method_ : methods){
					if(method_.getName().equalsIgnoreCase(methodName)){
						method = method_;
						break;
					}
				}
			}
		}catch(Exception e){}
		return method;
	}
}
