package cn.xglory.service.common.dao.entitydao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
@Inherited
public @interface Column {

	/**
	 * 注释
	 * @return
	 */
	String comment() default "";
	
	/**
	 * 是否主键
	 * @return
	 */
	boolean pk() default false;
	
	/**
	 * 字段名
	 * @return
	 */
	String field() default "";

	/**
	 * 数据类型
	 * @return
	 */
	String type() default "";
	
	/**
	 * 数据长度
	 * @return
	 */
	String length() default "";
	
	/**
	 * 是否必须
	 * @return
	 */
	boolean require() default false;
	
	/**
	 * Java数据类型
	 * @return
	 */
	String javaClass() default "";
}
