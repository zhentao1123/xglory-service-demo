package cn.xglory.service.common.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.xglory.service.common.annotation.BizController;
import cn.xglory.service.common.annotation.BizServiceImpl;
import cn.xglory.service.common.annotation.BizServiceMock;
import cn.xglory.service.common.base.CommonReq;
import cn.xglory.service.common.base.CommonRsp;
import cn.xglory.service.util.spring.SpringUtils;

public class BaseControllerAspect {
	
	private static Log logger = LogFactory.getLog(BaseControllerAspect.class);
	
	@SuppressWarnings("unchecked")
	protected Object aroundControllerMethod(ProceedingJoinPoint pjp, String defaultServiceImpPackage, String defaultServiceMockPackage)
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
		String path = "";
		String methodPath = "";
		String classPath = "";
		classPath = ((RequestMapping)targetClazz.getAnnotation(RequestMapping.class)).value()[0];
		methodPath = ((RequestMapping)targetMethod.getAnnotation(RequestMapping.class)).value()[0];
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
			String methodName = null;
			Class serviceImplClass = null;
			Class serviceMockClass = null;
			Object serviceImpl = null;
			Object serviceMock = null;
			Method method = null;
			//注解标注的Service实现类名
			String annServiceImplName = null;
			//注解标注的Service模拟类名
			String annServiceMockName = null;
			//注解标注的Service方法名
			String annServiceMethodName = null;

			Method serviceMethod = null;
			
			//--搜索匹配的Service---------------------------
			{
				//获取BizController标签指定的业务实现类及业务模拟类
				BizController classBizController = null;
				BizController methodBizController = null;
				String serviceImplSimpleName = null;
				String serviceMockSimpleName = null;
				
				try{
					classBizController = (BizController)targetClazz.getAnnotation(BizController.class);
				}catch(Exception e){}
				try{
					methodBizController = (BizController)targetMethod.getAnnotation(BizController.class);
				}catch(Exception e){}
				
				//取Service实现类实例
				{
					if(null!=methodBizController){//从方法注解取
						serviceImplSimpleName = methodBizController.serviceImplClass();
					}else{
						if(null!=classBizController){//从类注解取
							serviceImplSimpleName = classBizController.serviceImplClass();
						}else{//从目标类名取
							serviceImplSimpleName = targetClazz.getSimpleName().replace("Controller", "ServiceImpl");
						}
					}
					try{
						//有缺陷，但暂靠约定解决：带BizServiceImpl注解但受管bean必须避免同名，且以单例定义
						serviceImpl = getBeanListByAnnotationAndNameInSpring(BizServiceImpl.class, serviceImplSimpleName).get(0);
					}catch(Exception e){}
				}
				
				//取Service模拟类实例
				{
					if(null!=methodBizController){//从方法注解取
						serviceMockSimpleName = methodBizController.serviceMockClass();
					}else{
						if(null!=classBizController){//从类注解取
							serviceMockSimpleName = classBizController.serviceMockClass();
						}else{//从目标类名取
							serviceMockSimpleName = targetClazz.getSimpleName().replace("Controller", "ServiceMock");
						}
					}
					try{
						//有缺陷，但暂靠约定解决：带BizServiceImpl注解但受管bean必须避免同名，且以单例定义
						serviceMock = getBeanListByAnnotationAndNameInSpring(BizServiceMock.class, serviceMockSimpleName).get(0);
					}catch(Exception e){}
				}
			}
			
			
			//搜索匹配的Method
			
			
			
			
			//调用服务
//			Object invokeResult = null;
//			if(!req.isMock()){
//				serviceClass = SpringUtils.getContext().getClassLoader().loadClass(defaultServiceImpPackage+serviceImplName);
//			}else{
//				serviceClass = SpringUtils.getContext().getClassLoader().loadClass(defaultServiceMockPackage+serviceMockName);
//			}
//			serviceMethod = getClassMethod(serviceClass, defaultServiceMethodName, null);
//			invokeResult = serviceMethod.invoke(SpringUtils.getBean(serviceClass), req.getData());
			
			rsp = new CommonRsp<Object>();
//			rsp.setData(invokeResult);;
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
	
	public static void main(String[] args){
		System.out.println(BaseControllerAspect.class.getName());
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
				if(className.equals(classSimpleName)){
					beanList.add(classes.get(className));
				}
			}
		}
		return beanList;
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
