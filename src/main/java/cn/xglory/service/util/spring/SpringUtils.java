package cn.xglory.service.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware {
	
	private static ApplicationContext context;

	/**
	 * 此方法可以把ApplicationContext对象inject到当前类中作为一个静态成员变量。
	 */

	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static <T> T getBean(Class<T> clazz) {
		try {
			return getContext().getBean(clazz);
		} catch (Exception e) {
			return null;
		}
	}

	public static Object getBean(String beanId) {
		try {
			return getContext().getBean(beanId);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T getBean(String beanId, Class<T> clazz) {
		try {
			return getContext().getBean(beanId, clazz);
		} catch (Exception e) {
			return null;
		}
	}
}