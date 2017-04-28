package cn.xglory.service.common.controller.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.xglory.service.common.controller.annotation.BizController;
import cn.xglory.service.common.controller.annotation.BizServiceImpl;
import cn.xglory.service.common.controller.annotation.BizServiceMethod;
import cn.xglory.service.common.controller.annotation.BizServiceMock;
import cn.xglory.service.common.controller.base.CommonReq;
import cn.xglory.service.common.controller.base.CommonRsp;
import cn.xglory.service.common.exception.BaseServiceException;
import cn.xglory.service.util.spring.SpringUtils;

public class BaseControllerAspect {
	
	private static Logger logger = LoggerFactory.getLogger("BaseControllerAspect");
	
	@SuppressWarnings("unchecked")
	protected Object aroundControllerMethod(ProceedingJoinPoint pjp)
	{
		logger.debug("== aroundControllerMethod begin ==");
		long processBegin = new Date().getTime();
		
		//System.out.println(pjp.getArgs());
		//System.out.println(pjp.getKind());
		//System.out.println(pjp.getTarget());
		//System.out.println(pjp.getTarget().getClass().getSimpleName());
		//System.out.println(pjp.getThis());
		//System.out.println(pjp.toLongString());
		
		//System.out.println(pjp.getSignature());
		//System.out.println(pjp.getSignature().getName());
		//System.out.println(pjp.getSignature().getDeclaringType());
		//System.out.println(pjp.getSignature().getDeclaringTypeName());
		//System.out.println(pjp.getSignature().toShortString());
		//System.out.println(pjp.getSignature().toLongString());
		
		//System.out.println(pjp.getSourceLocation());
		//System.out.println(pjp.getStaticPart());
		
		Object[] args = pjp.getArgs();
		Class<?> targetClazz = pjp.getTarget().getClass();//当前横切目标类
		Method targetMethod = ((MethodSignature)pjp.getSignature()).getMethod();//当前横切目标方法
		
		Object result = null;
		CommonReq<Object> req = null;
		CommonRsp<Object> rsp = null;

		//打印接口URL日志
		try{
			String path = "";
			String methodPath = "";
			String classPath = "";
			classPath = ((RequestMapping)targetClazz.getAnnotation(RequestMapping.class)).value()[0];
			methodPath = ((RequestMapping)targetMethod.getAnnotation(RequestMapping.class)).value()[0];
			path = classPath + methodPath;
			logger.debug("path : " + path);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//处理参数
		try{
			req = (CommonReq<Object>)args[0];
			if(null==req.isMock()){ //默认调用真实实现
				req.setMock(false);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw BaseServiceException.buildParamErr();
		}
		
		//调用处理
		try {
			//result = pjp.proceed(args);
			//rsp = (CommonRsp<?>)result;
			
			//Controller类注解
			BizController classBizController = null;
			//Controller方法注解
			BizController methodBizController = null;
			
			String serviceSimpleName = null;
			Object service = null;
			String serviceMethodName = null;
			Method serviceMethod = null;
			
			//--获取BizController注解---------------------------
			{
				//获取BizController标签指定的业务实现类及业务模拟类
				try{
					classBizController = (BizController)targetClazz.getAnnotation(BizController.class);
				}catch(Exception e){}
				try{
					methodBizController = (BizController)targetMethod.getAnnotation(BizController.class);
				}catch(Exception e){}
			}
			
			//--获取Service执行方法名---------------------------
			if(null!=methodBizController){//从方法注解取
				serviceMethodName = methodBizController.serviceMethodName();
			}
			if(StringUtils.isBlank(serviceMethodName)){
				serviceMethodName = targetMethod.getName();
			}
			
			//--获取ServiceSimpleName---------------------------
			if(!req.isMock())//正式实现
			{
				//从方法注解取
				if(null!=methodBizController){
					serviceSimpleName = methodBizController.serviceImplClass();
				}
				//从类注解取
				if(StringUtils.isBlank(serviceSimpleName) && null!=classBizController){
					serviceSimpleName = classBizController.serviceImplClass();
				}
				//从目标类名取
				if(StringUtils.isBlank(serviceSimpleName)){
					serviceSimpleName = targetClazz.getSimpleName().replace("Controller", "ServiceImpl");
				}
				
				//有缺陷，但暂靠约定解决：带BizServiceImpl、BizServiceMock注解的受管bean必须避免同名，且以单例定义
				try{
					service = getBeanListByAnnotationAndNameInSpring(BizServiceImpl.class, serviceSimpleName).get(0);
				}catch(Exception e){
					e.printStackTrace();
					throw new BaseServiceException(BaseServiceException.CODE_COMM_FAIL, "获取Service失败");
				}
			}
			else//模拟实现
			{
				//从方法注解取
				if(null!=methodBizController){
					serviceSimpleName = methodBizController.serviceMockClass();
				}
				//从类注解取
				if(StringUtils.isBlank(serviceSimpleName) && null!=classBizController){
					serviceSimpleName = classBizController.serviceMockClass();
				}
				//从目标类名取
				if(StringUtils.isBlank(serviceSimpleName)){
					serviceSimpleName = targetClazz.getSimpleName().replace("Controller", "ServiceMock");
				}
				
				//有缺陷，但暂靠约定解决：带BizServiceImpl、BizServiceMock注解的受管bean必须避免同名，且以单例定义
				try{
					service = getBeanListByAnnotationAndNameInSpring(BizServiceMock.class, serviceSimpleName).get(0);
				}catch(Exception e){
					e.printStackTrace();
					throw new BaseServiceException(BaseServiceException.CODE_COMM_FAIL, "获取模拟Service失败");
				}
			}
			
			//搜索匹配的Method
			try{
				serviceMethod = getClassMethod(service.getClass(), serviceMethodName, BizServiceMethod.class, null);
			}catch(Exception e){
				e.printStackTrace();
				throw new BaseServiceException(BaseServiceException.CODE_COMM_FAIL, "匹配Service方法失败");
			}
			
			//调用服务
			if(null!=req.getData()){
				result = serviceMethod.invoke(service, req.getData());
			}else{
				result = serviceMethod.invoke(service);
			}
			
			rsp = new CommonRsp<Object>();
			rsp.setData(result);;
			rsp.init4Succeed();
			
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BaseServiceException(BaseServiceException.CODE_COMM_FAIL, "AOP调用失败");
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
	 * 在spring受管bean中匹配注解和名称，返回匹配成功的实例列表
	 * @param annotation
	 * @param classSimpleName
	 * @return
	 */
	private List<Object> getBeanListByAnnotationAndNameInSpring(Class<? extends Annotation> annotation, String classSimpleName){
		List<Object> beanList = new ArrayList<Object>();
		Map<String, Object> classes = SpringUtils.getContext().getBeansWithAnnotation(annotation);
		if(null!=classes && !classes.isEmpty()){
			for(String className : classes.keySet()){
				if(className.equalsIgnoreCase(classSimpleName)){
					beanList.add(classes.get(className));
				}
			}
		}
		return beanList;
	}
	
	/**
	 * 根据方法名（和参数类型）获取类中的方法;若同名优先取匹配注释的方法
	 * @param clazz 目标类
	 * @param methodName 方法名
	 * @param annotation 可选的匹配注释
	 * @param parameterTypes 可选的匹配方法参数
	 * @return
	 */
	private Method getClassMethod(Class clazz, String methodName, Class annotation, Class... parameterTypes){
		Method method = null;
		try{
			if(null!=parameterTypes){
				method = clazz.getMethod(methodName, parameterTypes);
			}else{
				Method[] methods = clazz.getMethods();
				List<Method> matchNameMethods = new ArrayList<Method>();
				for(Method method_ : methods){
					if(method_.getName().equalsIgnoreCase(methodName)){
						matchNameMethods.add(method_);
					}
				}
				if(matchNameMethods.size()>0){
					if(annotation!=null){
						for(Method method_ : matchNameMethods){
							Object ann = method_.getAnnotation(annotation);
							if(null!=ann){
								method = method_;
								break;
							}
						}
					}
					if(method==null){
						method = matchNameMethods.get(0);
					}
				}
			}
		}catch(Exception e){}
		return method;
	}
}
