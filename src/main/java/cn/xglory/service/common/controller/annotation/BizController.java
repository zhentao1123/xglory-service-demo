package cn.xglory.service.common.controller.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注设置Controller类或其中某方法关联的服务
 * @author zhangzhentao
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BizController {
	
	/**
	 * 业务实现类名
	 */
	String serviceImplClass() default "";
	
	/**
	 * 业务模拟类名
	 */
	String serviceMockClass() default "";
	
	/**
	 * 业务方法名
	 * @return
	 */
	String serviceMethodName() default "";
}
