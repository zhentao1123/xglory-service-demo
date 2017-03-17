package cn.xglory.service.util.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
 
public class AspectJUtil {
	
	/** 获取方法上的某个注解
	 * null 表示没有这个注解作用在方法上
	 * @param jp
	 * @param clazz
	 * @return
	 */
	public static <T extends Annotation> T  getMethodAnnotation(JoinPoint jp, Class<T> clazz) {
		 
		Method method = null;
		MethodSignature signature = (MethodSignature) jp.getSignature();
		// 这里不能直接用getMethod() 方法,因为WAS6.1 自带了aspectj的低版.产生了冲突
		// 暂时没有发现spring使用的aop出现这样的情况
		String methodName = signature.getName()  ;
		try {
			method =  signature.getDeclaringType().getMethod(methodName, signature.getParameterTypes())    ;//signature2.getMethod();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		if (method != null && method.isAnnotationPresent(clazz)) {
			 return  (T)method.getAnnotation(clazz);
		}
		return null;
	}
}
